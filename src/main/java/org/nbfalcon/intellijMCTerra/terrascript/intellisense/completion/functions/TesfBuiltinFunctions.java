package org.nbfalcon.intellijMCTerra.terrascript.intellisense.completion.functions;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TesfBuiltinFunctions {
    private final Map<String, FunctionDescription> builtins = new HashMap<>();
    private LookupElement[] completions = new LookupElement[0];

    public TesfBuiltinFunctions() {
        try {
            final FunctionDescription[] funcs = loadBuiltins();
            this.completions = new LookupElement[builtins.size()];
            int i = 0;
            for (FunctionDescription func : funcs) {
                completions[i] = LookupElementBuilder.create(func.name).withIcon(AllIcons.Nodes.Method);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TesfBuiltinFunctions getInstance() {
        return ApplicationManager.getApplication().getService(TesfBuiltinFunctions.class);
    }

    public LookupElement[] getCompletions() {
        return completions;
    }

    private FunctionDescription[] loadBuiltins() throws IOException {
        final InputStream rsc = this.getClass().getResourceAsStream("/TesfBuiltins.json");
        final FunctionDescription[] funcs = FunctionDescription.read(rsc);
        for (FunctionDescription func : funcs) {
            builtins.put(func.name, func);
        }
        return funcs;
    }

    public @Nullable FunctionDescription getDescription(String name) {
        return builtins.get(name);
    }
}
