package org.nbfalcon.intellijMCTerra.terrascript.intellisense.completion.functions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nbfalcon.intellijMCTerra.terrascript.psi.util.TesfPsiUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FunctionDescription {
    public String name;
    public TesfPsiUtil.TesfType returnType;

    public NormalParameter[] args;
    public Parameter varArg;

    public static class Parameter {
        public String name;
        public TesfPsiUtil.TesfType type;
    }

    public static class NormalParameter extends Parameter {
        public boolean isOptional;
    }

    public static class DescFile {
        public FunctionDescription[] funcs;
    }

    public static FunctionDescription[] read(InputStream in) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new InputStreamReader(in), DescFile.class).funcs;
    }
}
