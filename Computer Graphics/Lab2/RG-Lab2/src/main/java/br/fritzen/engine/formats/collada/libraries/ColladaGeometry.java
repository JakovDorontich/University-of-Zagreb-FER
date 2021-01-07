package br.fritzen.engine.formats.collada.libraries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import br.fritzen.engine.formats.collada.utils.ColladaUtils;

/**
 * Main limitations:
 * <ul>
 * <li>Work just from triangulated models. vcount = 3</li>
 * <li>Ignoring vertex colors.</li>
 * </ul>
 * 
 * @author Vinicius
 *
 */
public class ColladaGeometry {

	private final static Logger LOG = Logger.getLogger(ColladaGeometry.class.getName());
		
	private String id;

	private String name;

	private HashMap<String, ArrayList<Float>> sources;

	private ArrayList<Vector3f> vertices;

	private ArrayList<Vector2f> texCoords;

	private ArrayList<Vector3f> normals;

	private ArrayList<Integer> indices;

	private ArrayList<Integer> indicesIndexedModel;
	
	private HashMap<String, Integer> semanticOffset;

	
	/**
	 * Constructor method to parse a geometry node in collada file.
	 * 
	 * @param {@link Node} geometryNode
	 */
	public ColladaGeometry(Node geometryNode) {

		sources = new HashMap<String, ArrayList<Float>>();
		semanticOffset = new HashMap<String, Integer>();
		
		vertices = new ArrayList<Vector3f>();
		normals = new ArrayList<Vector3f>();
		texCoords = new ArrayList<Vector2f>();
		indices = new ArrayList<Integer>();
		
		parseGeometryNode(geometryNode);
	}

	
	private ColladaGeometry parseGeometryNode(Node geometryNode) {

		this.id = geometryNode.getAttributes().getNamedItem("id").getTextContent();
		this.name = geometryNode.getAttributes().getNamedItem("name").getTextContent();
		
		LOG.info("Loading geometry... ID: " + this.id + "\tName: " + this.name);
		
		Node mesh = null;
		for (int i = 0; i < geometryNode.getChildNodes().getLength(); i++) {
			if (geometryNode.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE) {
				mesh = geometryNode.getChildNodes().item(i);
				break;	//TODO currently considering one mesh per geometry
			}
		}

		for (int i = 0; i < mesh.getChildNodes().getLength(); i++) {
			
			Node curNode = mesh.getChildNodes().item(i);

			if (curNode.getNodeType() != Node.ELEMENT_NODE)
				continue;

			if (curNode.getNodeName().equals("source")) {

				String sourceId = curNode.getAttributes().getNamedItem("id").getTextContent();

				String values = ((Text) curNode.getChildNodes().item(1).getFirstChild()).getTextContent();
				String valuesSplitted[] = values.split(" ");
				
				ArrayList<Float> currentFloatArray = new ArrayList<Float>();
				
				for (String s : valuesSplitted) {
					if (!s.trim().isEmpty()) {
						currentFloatArray.add(Float.parseFloat(s));
					}
				}

				this.sources.put(sourceId, currentFloatArray);

			} else if (curNode.getNodeName().equals("vertices")) {

				for (int j = 0; j < curNode.getChildNodes().getLength(); j++) {
					
					Node curChild = curNode.getChildNodes().item(j);
					
					if (curChild.getNodeType() != Node.ELEMENT_NODE)
						continue;
					
					String childName = curNode.getChildNodes().item(j).getNodeName();

					if (childName.equals("input")) {
						
						String semantic = curChild.getAttributes().getNamedItem("semantic").getTextContent();
						String sourceToMap = curChild.getAttributes().getNamedItem("source").getTextContent()
								.substring(1);
						
						if (semantic.equals("POSITION")) {
							this.sources.put(curNode.getAttributes().getNamedItem("id").getTextContent(),
									this.sources.get(sourceToMap));
						}
					}
					
				}

			} else if (curNode.getNodeName().equals("polylist") || curNode.getNodeName().equals("triangles")) {

				for (int j = 0; j < curNode.getChildNodes().getLength(); j++) {
					
					Node curChild = curNode.getChildNodes().item(j);
					if (curChild.getNodeType() == Node.ELEMENT_NODE) {
						String childName = curNode.getChildNodes().item(j).getNodeName();

						if (childName.equals("input")) {

							String semantic = curChild.getAttributes().getNamedItem("semantic").getTextContent();
							String sourceToMap = curChild.getAttributes().getNamedItem("source").getTextContent()
									.substring(1);
							
							int offset = Integer
									.parseInt(curChild.getAttributes().getNamedItem("offset").getTextContent());

							semanticOffset.put(semantic + offset, offset);
							
							if (semantic.equals("VERTEX")) {
								
								if (this.sources.containsKey(sourceToMap)) {
									vertices.addAll(ColladaUtils.parseFloatToVec3(this.sources.get(sourceToMap)));
								} else {
									LOG.warning("Problem parsing collada geometry. There is no source with name: " + sourceToMap);
								}
								
							} else  if (semantic.equals("NORMAL")) {
								
								if (this.sources.containsKey(sourceToMap)) {
									normals.addAll(ColladaUtils.parseFloatToVec3(this.sources.get(sourceToMap)));
								} else {
									LOG.warning("Problem parsing collada geometry. There is no source with name: " + sourceToMap);
								}
								
							} else if (semantic.equals("TEXCOORD")) {
								
								if (this.sources.containsKey(sourceToMap)) {
									texCoords.addAll(ColladaUtils.parseFloatToVec2(this.sources.get(sourceToMap)));
								} else {
									LOG.warning("Problem parsing collada geometry. There is no source with name: " + sourceToMap);
								}
								
							} 
							
						} else if (childName.equals("vcount")) {
						
							//TODO - ignoring for now. all objects are triangulates (vcount = 3)
						
						} else if (childName.equals("p")) {
							
							String valuesInString = curChild.getChildNodes().item(0).getNodeValue();
							this.indices.addAll(ColladaUtils.parseStringToInts(valuesInString));
														
						} 

					}
				}
			}

		}

		this.indicesIndexedModel = parseIndices(this.indices);
		
		return this;
	}

	
	private ArrayList<Integer> parseIndices(final ArrayList<Integer> indices) {
		
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		int quantSemantics = semanticOffset.entrySet().size();
		
		for (int i = 0; i < indices.size() - quantSemantics + 1; i += quantSemantics) {
			
//			result.add(indices.get(i + semanticOffset.get("VERTEX")));
//			result.add(indices.get(i + semanticOffset.get("VERTEX")));
//			result.add(indices.get(i + semanticOffset.get("VERTEX")));
			result.add(indices.get(i));
			result.add(indices.get(i+1));
			result.add(indices.get(i+2));
		}
		
		return result;
		
	}

	
	/**
	 * Get vertices for a Geometry in library_geometries
	 * 
	 * @return ArrayList<Vector3f> for all vertices in the current geometry
	 */
	public ArrayList<Vector3f> getVertices() {
		return vertices;
	}

	
	/**
	 * Get texture coordinates for a Geometry in library_geometries
	 * 
	 * @return ArrayList<Vector2f> for all texture coordinates in the current geometry
	 */
	public ArrayList<Vector2f> getTexCoords() {
		return texCoords;
	}

	
	/**
	 * Get normals for a Geometry in library_geometries
	 * 
	 * @return ArrayList<Vector3f> for all normals in the current geometry
	 */
	public ArrayList<Vector3f> getNormals() {
		return normals;
	}

	
	/**
	 * Get indices all indices for a Geometry in library_geometries
	 * 
	 * @return ArrayList<Vector3f> for all positions in the current geometry
	 */
	public ArrayList<Integer> getIndices() {
		return indices;
	}
	
	
	/**
	 * Get indices (VERTEX, NORMAL, TEXTURE) indices for a Geometry in library_geometries
	 * 
	 * @return ArrayList<Vector3f> for all positions in the current geometry
	 */
	public ArrayList<Integer> getIndicesIndexedModel() {
		return indicesIndexedModel;
	}


	/**
	 * 
	 * Use this to parse for your own implementation. This method doesn't change/filter data loaded from collada file.
	 * 
	 * @return HashMap<String, ArrayList<Float>> with all sources in current geometry.
	 */
	public HashMap<String, ArrayList<Float>> getSources() {
		return sources;
	}


	/**
	 * 
	 * Use this to parse for your own implementation. This method doesn't change/filter data loaded from collada file.
	 * 
	 * @return HashMap<String, Integer> with all semantic offsets to parse your model.
	 */
	public HashMap<String, Integer> getSemanticOffset() {
		return semanticOffset;
	}

	
	
}
