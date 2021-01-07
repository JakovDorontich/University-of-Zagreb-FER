package br.fritzen.engine.formats.collada;

import java.util.ArrayList;
import java.util.List;

import br.fritzen.engine.formats.collada.libraries.ColladaAsset;
import br.fritzen.engine.formats.collada.libraries.ColladaGeometry;

public class ColladaModel {

	ColladaAsset asset;
	
	List<ColladaGeometry> libraryGeometries;
	
	
	public ColladaModel() {
		libraryGeometries = new ArrayList<ColladaGeometry>();
	}

	
	public List<ColladaGeometry> getLibraryGeometries() {
		return libraryGeometries;
	}
		
}
