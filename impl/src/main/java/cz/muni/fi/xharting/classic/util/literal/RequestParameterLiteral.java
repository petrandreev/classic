package cz.muni.fi.xharting.classic.util.literal;

import javax.enterprise.util.AnnotationLiteral;

import cz.muni.fi.xharting.classic.util.servlet.http.RequestParameter;

@SuppressWarnings("all")
public class RequestParameterLiteral extends AnnotationLiteral<RequestParameter> implements RequestParameter {

    private String value;

    public RequestParameterLiteral(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
