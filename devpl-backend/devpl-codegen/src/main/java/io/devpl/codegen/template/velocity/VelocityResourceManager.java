package io.devpl.codegen.template.velocity;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.ResourceManager;

public class VelocityResourceManager implements ResourceManager {
    @Override
    public void initialize(RuntimeServices rs) {

    }

    @Override
    public Resource getResource(String resourceName, int resourceType, String encoding) throws ResourceNotFoundException, ParseErrorException {
        return null;
    }

    @Override
    public String getLoaderNameForResource(String resourceName) {
        return null;
    }
}
