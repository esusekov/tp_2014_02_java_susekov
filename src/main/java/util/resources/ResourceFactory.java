package util.resources;

import util.SAX.XMLReader;
import java.util.HashMap;
import java.util.Map;

public class ResourceFactory {
    private static ResourceFactory instance;
    private Map<String, Resource> resources = new HashMap<>();

    private ResourceFactory(){
    }

    public static ResourceFactory getInstance(){
        if (instance == null){
            instance = new ResourceFactory();
        }
        return instance;
    }

    public Resource get(String path) {
        Resource res = instance.resources.get(path);
        if (res != null)
            return res;
        addResource(path, getResource(path));
        return get(path);
    }

    public void addResource(String path, Resource resource){
        instance.resources.put(path, resource);
    }

    public Resource getResource(String path){
        return (Resource)XMLReader.readXML(path);
    }
}
