package org.eclipse.xtext.example.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.xtext.ui.editor.model.IXtextDocument;

public class PrintNodeHandler extends AbstractHandler {
    @Override
    public Object execute(ExecutionEvent event) {
        IEditorPart editor = HandlerUtil.getActiveEditor(event);
        if (editor instanceof XtextEditor) {
            XtextEditor xtextEditor = (XtextEditor) editor;
            IXtextDocument doc = xtextEditor.getDocument();

            doc.readOnly((XtextResource res) -> {
                EObject root = res.getContents().get(0);
                INode rootNode = NodeModelUtils.findActualNodeFor(root);
                for (INode node : rootNode.getAsTreeIterable()) {
                    if (node instanceof ILeafNode) {
                        System.out.print(((ILeafNode) node).getText());
                    }
                }
                return null;
            });
        }
        return null;
    }
}