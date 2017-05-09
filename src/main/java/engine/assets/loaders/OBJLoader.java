package engine.assets.loaders;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.assets.Mesh;
import engine.utils.ResourceUtils;

/**
 * Specific game object loader to load OBJ file types
 * 
 * @author brandon.porter
 *
 */
class OBJLoader {
	private static final String VERTEX = "v";
	private static final String TEXTURE_COORDINATE = "vt";
	private static final String VERTEX_NORMAL = "vn";
	private static final String FACE = "f";

	/**
	 * Loads the vbo data for a mesh file
	 * 
	 * @param fileName
	 *            name of OBJ file to load
	 * @return new vbo data representing the OBJ file
	 * @throws Exception
	 */
	public static Mesh.MeshVBOData loadVBOData(String fileName) throws Exception {
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> textureCoords = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<VertexFace> faces = new ArrayList<>();

		// Read in the model as a list of strings
		List<String> lines = ResourceUtils.loadModel(fileName);

		for (String line : lines) {
			// Split on 1 (or more) spaces
			String[] tokens = line.split("\\s+");
			switch (tokens[0]) {

			// vertex e.g. "v 1.000 0.500 -1.000"
			case VERTEX:
				vertices.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]),
						Float.parseFloat(tokens[3])));
				break;

			// texture coordinates e.g. "vt 0.500 1.000"
			case TEXTURE_COORDINATE:
				textureCoords.add(new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));
				break;

			// vertex normals e.g. "vn 0.000 1.000 0.000"
			case VERTEX_NORMAL:
				normals.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]),
						Float.parseFloat(tokens[3])));
				break;

			// Vertex faces e.g. "f 11/1/1 17/2/1 13/3/1"
			// (Vertex/Texture/Normal - a face is composed by a list of indices
			// groups - triangles have 3 indices groups)
			// For faces with no texture coords: e.g. "f 11//1 17//1 13//1"
			case FACE:
				faces.add(new VertexFace(tokens[1], tokens[2], tokens[3]));
				break;

			// Ignore all other lines
			default:
				break;
			}
		}

		// Process each vertex list into a correctly ordered and mapped array
		// Returns a new vbo containing that data
		return parseEachVertexListIntoVBOArrays(vertices, textureCoords, normals, faces);
	}

	/*
	 * Processes each vertex face to map the textures and normals to an index array. This returns
	 * a container vbo with the wrapped data
	 */
	private static Mesh.MeshVBOData parseEachVertexListIntoVBOArrays(List<Vector3f> positionVectors,
			List<Vector2f> texCoordVectors, List<Vector3f> normalVectors, List<VertexFace> vertexFaces) {

		// Define empty arrays
		// Each vertex face has 3 mappings, Positions and normals have 3 values
		// (x,y,z), textures have 2 values (x,y)
		int vertexCount = positionVectors.size();
		int[] indices = new int[vertexFaces.size() * 3];
		float[] positions = new float[vertexCount * 3];
		float[] texCoords = new float[vertexCount * 2];
		float[] normals = new float[vertexCount * 3];

		// MeshLoader.MeshVBOData vboData = new
		// MeshLoader.MeshVBOData(positions, texCoords, normals);

		// Create position array in the order it has been declared
		for (int i = 0; i < vertexCount; i++) {
			Vector3f pos = positionVectors.get(i);
			positions[i * 3] = pos.x;
			positions[i * 3 + 1] = pos.y;
			positions[i * 3 + 2] = pos.z;
		}

		// Loop over each vertex face to load the correct mapped index for each
		// vertex into each array
		int i = 0;
		for (VertexFace face : vertexFaces) {
			for (IdxGroup idxGroup : face.getFaceVertexIndices())
				indices[i++] = idxGroup.mapGroup(texCoordVectors, normalVectors, texCoords, normals);
		}

		// Return the new mesh vbo
		return new Mesh.MeshVBOData (positions, texCoords, normals, indices);
	}

	/*
	 * Represents a vertex face
	 * 
	 * Vertex faces e.g. "f 11/1/1 17/2/1 13/3/1" (Vertex/Texture/Normal - a
	 * face is composed by a list of indices groups - triangles have 3 indices
	 * groups) For faces with no texture coords: e.g. "f 11//1 17//1 13//1"
	 */
	private static class VertexFace {
		// List of index groups for a face triangle (3 vertices per face)
		private final IdxGroup[] _idxGroups = new IdxGroup[3];

		/**
		 * Creates a new vertex face Each vertex group should be in the form
		 * "Vertex/TextureCoords/Normal"
		 * 
		 * @param vg1
		 *            - x vertex group
		 * @param vg2
		 *            - y vertex group
		 * @param vg3
		 *            - z vertex group
		 * @throws Exception
		 */
		public VertexFace(String vg1, String vg2, String vg3) throws Exception {
			_idxGroups[0] = parseGroup(vg1);
			_idxGroups[1] = parseGroup(vg2);
			_idxGroups[2] = parseGroup(vg3);
		}

		/**
		 * @return the mapped indices for this face
		 */
		public IdxGroup[] getFaceVertexIndices() {
			return _idxGroups;
		}

		/**
		 * Parse the vertex group into indexes (we have to subtract 1 for each
		 * value because values in OBJ files are 1-indexed based)
		 * 
		 * @param vertGroup
		 * @return
		 * @throws Exception
		 */
		private IdxGroup parseGroup(String vertGroup) throws Exception {
			IdxGroup idxGroup = new IdxGroup();
			String[] tokens = vertGroup.split("/");

			// Every group SHOULD be length 3 (position, texture, normal)
			if (tokens.length != 3) {
				throw new Exception(
						"Invalid Vertex Face: Trying to create vertex face with index group length != 3, act length is: "
								+ tokens.length);
			}

			// Set position index
			idxGroup.posIdx = Integer.parseInt(tokens[0]) - 1;

			// Set texture coordinate index
			String texCoord = tokens[1];
			idxGroup.texCoordIdx = texCoord.length() > 0 ? Integer.parseInt(texCoord) - 1 : IdxGroup.NO_VALUE;

			// Set vertex normal index
			idxGroup.vecNormalIdx = Integer.parseInt(tokens[2]) - 1;

			return idxGroup;
		}
	}

	/*
	 * Keeps track of the index mappings for the vertex faces This is necessary
	 * for storing our indices VBO to reduce vertex duplication
	 */
	private static class IdxGroup {
		public static final int NO_VALUE = -1;

		public int posIdx = NO_VALUE;
		public int texCoordIdx = NO_VALUE;
		public int vecNormalIdx = NO_VALUE;

		/**
		 * Maps this index group to into a correctly ordered list of the
		 * correctly mapped textures and normals
		 * 
		 * @param texCoordVectors
		 * @param normalVectors
		 * @param texCoords
		 * @param normals
		 * @return the index position
		 */
		public int mapGroup(List<Vector2f> texCoordVectors, List<Vector3f> normalVectors, float[] texCoords,
				float[] normals) {
			// Reorder texture coordinates
			if (texCoordIdx >= 0) {
				Vector2f texCoord = texCoordVectors.get(texCoordIdx);
				texCoords[posIdx * 2] = texCoord.x;
				// texture coordinates are in UV format, so y coordinates
				// need to be calculated 1 minus the original texture y
				// value
				texCoords[posIdx * 2 + 1] = 1 - texCoord.y;
			}

			// Reorder vector normals
			if (vecNormalIdx >= 0) {
				Vector3f vecNorm = normalVectors.get(vecNormalIdx);
				normals[posIdx * 3] = vecNorm.x;
				normals[posIdx * 3 + 1] = vecNorm.y;
				normals[posIdx * 3 + 2] = vecNorm.z;
			}

			// return position index for the vertex coords
			return posIdx;
		}
	}
}
