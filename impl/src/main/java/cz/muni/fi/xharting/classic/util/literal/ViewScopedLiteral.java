package cz.muni.fi.xharting.classic.util.literal;

import javax.enterprise.util.AnnotationLiteral;
import javax.faces.view.ViewScoped;

/**
 * JSF 2.2 {@link ViewScoped} literal.
 *
 * @author pan
 *
 */
public class ViewScopedLiteral extends AnnotationLiteral<ViewScoped> implements ViewScoped {

    private static final long serialVersionUID = -5985312989344415230L;

    public static final ViewScoped INSTANCE = new ViewScopedLiteral();

}
