package org.eclipse.xtext.example.tools;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.example.DomainmodelStandaloneSetup;
import org.eclipse.xtext.example.domainmodel.AbstractElement;
import org.eclipse.xtext.example.domainmodel.DataType;
import org.eclipse.xtext.example.domainmodel.DomainModel;
import org.eclipse.xtext.example.domainmodel.Entity;
import org.eclipse.xtext.example.domainmodel.Feature;
import org.eclipse.xtext.example.domainmodel.Import;
import org.eclipse.xtext.example.domainmodel.PackageDeclaration;
import org.eclipse.xtext.example.domainmodel.Type;
import org.eclipse.xtext.example.domainmodel.TypeRef;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.impl.AbstractNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

public class EntityTraversal {
  public static void printPackageDeclaration(PackageDeclaration pd) {
    EList<AbstractElement> list = pd.getElements();
    for (AbstractElement ae : list) {
      System.out.print(">");
      printAbstractElement(ae);
    }
  }

  public static void printTypeRef(TypeRef typeRef) {
    System.out.println("+ TypeRef type");
    printType(typeRef.getReferenced());
  }

  public static void printFeature(Feature feature) {
    System.out.print("+ Feature name: " + feature.getName());
    System.out.println(" - typeRef");
    printTypeRef(feature.getType());
  }

  public static void printEntity(Entity entity) {
    System.out.print(">");
    System.out.print("Entity: " + entity.getName());
    System.out.println(" - extends: " + entity.getSuperType());
    EList<Feature> features = entity.getFeatures();
    for (Feature feature : features) {
      printFeature(feature);
    }
  }

  public static void printDataType(DataType dt) {
    System.out.print(">");
    System.out.println("datatype: " + dt.getName());
  }

  public static void printType(Type type) {
    if (type instanceof Entity) {
      Entity entity = (Entity) type;
      printEntity(entity);
    } else if (type instanceof DataType) {
      DataType dataType = (DataType) type;
      printDataType(dataType);
    }
  }

  public static void printAbstractElement(AbstractElement abstractElement) {
    System.out.print(">");
    if (abstractElement instanceof PackageDeclaration) {
      PackageDeclaration pd = (PackageDeclaration) abstractElement;
      System.out.println("PackageDeclaration: " + pd.getName());
      printPackageDeclaration(pd);
    } else if (abstractElement instanceof Type) {
      printType((Type)abstractElement);
    } else if (abstractElement instanceof Import) {
      Import ip = (Import) abstractElement;
      System.out.println("Import: " + ip.getImportedNamespace());
    }

  }

  public static void printDomainModel(DomainModel dm) {
    EList<AbstractElement> abstractElements = dm.getElements();

    for (AbstractElement abstractElement : abstractElements) {
      System.out.print(">");
      printAbstractElement(abstractElement);
    }
  }
  
  public static void printASTNode(DomainModel dm) {
    INode rootINode = NodeModelUtils.findActualNodeFor(dm);
    StringBuffer text = new StringBuffer();
    for (INode node : rootINode.getAsTreeIterable()) {
      if (node instanceof ILeafNode) {
        text.append(((ILeafNode)node).getText());
      }
    }
    System.out.println(text);
  }

  public static void main(String[] args) throws Exception {
    // Initialize DSL environment
    Injector injector = new DomainmodelStandaloneSetup().createInjectorAndDoEMFRegistration();

    ResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
    Resource resource = resourceSet.getResource(URI.createFileURI("src/model/example.dmodel"), true);
    EObject eobject = resource.getContents().get(0);

    DomainModel dm = (DomainModel) eobject;
//    printDomainModel(dm);
    printASTNode(dm);
  }
}