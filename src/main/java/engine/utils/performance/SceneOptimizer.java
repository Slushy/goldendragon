package engine.utils.performance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.joml.Matrix4fc;
import org.joml.Vector4f;

import engine.common.GameObject;
import engine.graphics.components.MeshRenderer;
import engine.graphics.geometry.Material;
import engine.graphics.geometry.Mesh;
import engine.resources.loaders.MeshLoader;
import engine.utils.Debug;

/**
 * Contains methods to optimize game objects at will
 * 
 * @author Brandon Porter
 *
 */
public final class SceneOptimizer {
	private static final Vector4f _tempVec = new Vector4f();
	
	// Static class
	private SceneOptimizer() {
	}

	/**
	 * Batches a gameobject's children by looping through every child looking
	 * for a renderer with like materials. For every child game object that has
	 * a renderer with the same material, its mesh will be merged into a bigger
	 * mesh and combined into 1 game object. This should only be used on game
	 * objects that are within the same area in the game; the reason is because
	 * each individual object cannot be culled anymore, as the whole object must
	 * be culled as one. Each merged game object also loses its mobility and can
	 * longer be referenced by itself.
	 * 
	 * It only merges meshes that are on the same level of child. For example, a
	 * game object's children will be checked for merging, but their children
	 * will not be compared. If recurse is true, the children's children will
	 * also be compared, but only with themselves and not with their parents.
	 * 
	 * i.e. A game object hierarchy will remain after batchChildren is called.
	 * Only children on the same level will be compared with one another for
	 * merging. NOTE: The root game object based in will not be compared.
	 * 
	 * @param root
	 *            the parent game object of all the like children to be compared
	 * @param recurse
	 *            whether to also batch the children's children
	 * @return the root game object with its children batched.
	 */
	public static GameObject batchChildren(GameObject root, boolean recurse) {
		// Build the children of the root
		buildBatches(root);

		// Batch the children recursively if recurse is true
		if (recurse) {
			for (GameObject child : root.getChildren())
				batchChildren(child, true);
		}

		return root;
	}

	/*
	 * Loops through every "batchable" game object and looks for renderers with
	 * like materials and stores them in a map keyd by the material. For every
	 * unique material we merge the game objects, so every material should only
	 * have 1 game object outcome
	 */
	private static ArrayList<GameObject> buildBatches(GameObject root) {
		ArrayList<GameObject> batchedGameObjects = new ArrayList<>();
		HashMap<Material, List<GameObject>> materialObjectMap = new HashMap<>();

		// Build the material renderer map by combining like materials. So each
		// similar material will have a list of game objects to combine
		for (GameObject obj : root.getChildren()) {

			// For objects with no renderers, they cannot be batched so we just
			// add them to the return list
			if (obj.getRenderer() == null || obj.getRenderer().getMaterial() == null) {
				batchedGameObjects.add(obj);
				continue;
			}

			List<GameObject> matList = materialObjectMap.get(obj.getRenderer().getMaterial());
			if (matList == null) {
				// We couldn't find same instance, so lets compare for same data
				for (Material mat : materialObjectMap.keySet()) {
					if (obj.getRenderer().getMaterial().compare(mat)) {
						matList = materialObjectMap.get(mat);
					}
				}
			}

			// If we couldn't find a like material, create a new spot
			if (matList == null) {
				matList = new ArrayList<GameObject>();
				materialObjectMap.put(obj.getRenderer().getMaterial(), matList);
			}

			// Finally, add that game object to the material
			matList.add(obj);
		}

		// Loop over each material batchable set
		for (Entry<Material, List<GameObject>> entry : materialObjectMap.entrySet()) {
			Material mat = entry.getKey();
			List<GameObject> matObjects = entry.getValue();

			// If we only have one game object, then just add it to the list and
			// continue
			if (matObjects.size() == 1) {
				batchedGameObjects.add(matObjects.get(0));
				continue;
			}
			
			// Create a new game object representing the combined mesh
			GameObject newObj = new GameObject("Batched Game Object");
			
			// Get 1 mesh from the list of game objects
			Mesh combinedMesh = combineMeshes(newObj, matObjects);
			
			// Add the renderer
			newObj.addComponent(new MeshRenderer(combinedMesh, mat));
			newObj.setParent(root);
			
			// Add the new game object to the return list
			batchedGameObjects.add(newObj);
		}

		return batchedGameObjects;
	}

	/*
	 * Takes a list of gameObjects and transforms them into world space and
	 * finally combines their meshes into one
	 */
	private static Mesh combineMeshes(GameObject newGameObject, List<GameObject> gameObjects) {
		Mesh.MeshVBOData[] vboDataList = new Mesh.MeshVBOData[gameObjects.size()];

		Debug.log("About to loop over every object");
		
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObject gameObj = gameObjects.get(i);
			Mesh.MeshVBOData vboData = gameObj.getRenderer().getMesh().getVBOData();

			// 1. Copying vbo data from current mesh
			float[] positions = vboData.vertexPositions.clone();
			float[] texCoords = vboData.textureCoords.clone();
			float[] normals = vboData.vertexNormals.clone();
			int[] indices = vboData.indices.clone();

			// 2. Transformations
			Matrix4fc transform = gameObj.getTransform().getLocalToWorldMatrix();
			transformComponent(positions, transform, true);
			transformComponent(normals, transform, false);
			// We increase the indices offset to point at the actual vertices
			// that it represents, otherwise the indices would be pointing at
			// the first set of vertices
			for (int j = 0; j < indices.length; j++) {
				indices[j] += i * (positions.length / 3);
			}

			// Add the cloned and transformed vbo data to the list
			vboDataList[i] = new Mesh.MeshVBOData(positions, texCoords, normals, indices);
			
			// For each child on the old game objects set their parent to the
			// new batched game object
			for (GameObject child : gameObj.getChildren())
				child.setParent(newGameObject);
			gameObj.setParent(null);
		}
		
		Debug.log("Done looping");
		
		// Loads the vbo list into one mesh
		return MeshLoader.loadCombinedMesh(vboDataList);
	}

	/*
	 * Multiplies the data points against the transform to get the points in
	 * world space
	 */
	private static float[] transformComponent(float[] data, Matrix4fc transform, boolean includeTranslation) {
		int w = includeTranslation ? 1 : 0;
		for (int i = 0; i < data.length / 3; i++) {
			_tempVec.x = data[i * 3 + 0];
			_tempVec.y = data[i * 3 + 1];
			_tempVec.z = data[i * 3 + 2];
			_tempVec.w = w;

			_tempVec.mul(transform);

			data[i * 3 + 0] = _tempVec.x;
			data[i * 3 + 1] = _tempVec.y;
			data[i * 3 + 2] = _tempVec.z;
		}

		return data;
	}
}