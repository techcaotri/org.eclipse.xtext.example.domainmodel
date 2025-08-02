package org.eclipse.xtext.example.tools;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.example.DomainmodelStandaloneSetup;
import org.eclipse.xtext.nodemodel.*;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.*;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsProvider;
import org.eclipse.xtext.util.CancelIndicator;

import org.eclipse.emf.common.util.URI;
import com.google.inject.Injector;

import java.io.File;
import java.util.Collections;

public class NodePrinter {
    public static void main(String[] args) throws Exception {
        // Initialize DSL environment
        Injector injector = new DomainmodelStandaloneSetup().createInjectorAndDoEMFRegistration();
        XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);

        // Path to your .dmodel file
        File file = new File("src/model/example.dmodel");
        URI uri = URI.createFileURI(file.getAbsolutePath());

        // Load resource
        Resource resource = resourceSet.getResource(uri, true);
        resource.load(null);

        // Get the root model object
        EObject root = resource.getContents().get(0);

        // Traverse and print node model
        INode rootNode = NodeModelUtils.findActualNodeFor(root);
        for (INode node : rootNode.getAsTreeIterable()) {
            if (node instanceof ILeafNode) {
                System.out.print(((ILeafNode) node).getText());
            }
        }
    }
}
