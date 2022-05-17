package br.pucrio.opus.smells.ast.visitors;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * Receives a MethodDeclaration as input and returns all external methods calls made by the input method
 * @author Daniel Coutinho (Based on LocalMethodCallVisitor by Diego Cedrim)
 */
public class ExternalMethodCallCollector extends CollectorVisitor<IMethodBinding> {

	/**
	 * Type that declares the method being visited
	 */
	private ITypeBinding declaringTypeBinding;

	public ExternalMethodCallCollector(TypeDeclaration declaringType) {
		this.declaringTypeBinding = declaringType.resolveBinding();
	}
	
	@Override
	public boolean visit(MethodInvocation node) {
		if (this.declaringTypeBinding == null) {
			return false;
		}
		IMethodBinding methodBinding = node.resolveMethodBinding();
		if (methodBinding == null) {
			return false;
		}

		ITypeBinding typeBinding = methodBinding.getDeclaringClass();
		if (typeBinding == null) {
			return false;
		}
		if (!typeBinding.isEqualTo(this.declaringTypeBinding)) {
			super.addCollectedNode(methodBinding);
		}
		return true;
	}
}
