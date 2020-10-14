package com.ts.judge.provider.flow.script;

import groovy.lang.GroovyClassLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ScriptCache {

    private Map<String, IInputScript> inputScriptCache = new ConcurrentHashMap<>();
    private Map<String, IOutputScript> outputScriptCache = new ConcurrentHashMap<>();
    private IInputScript defaultInputScript = new DefaultInputScript();
    private IOutputScript defaultOutputScript = new DefaultOutputScript();

    private GroovyClassLoader groovyClassLoader = new GroovyClassLoader();


    public IInputScript getInputScript(Integer definitionId, String name, String scriptText) throws IllegalAccessException, InstantiationException {
        if (StringUtils.isEmpty(scriptText)) {
            return defaultInputScript;
        }
        String key = "s-" + definitionId + "-" + name;
        IInputScript inputScript = inputScriptCache.getOrDefault(key, null);
        if (inputScript == null) {
            Class aClass = groovyClassLoader.parseClass(scriptText);
            inputScript = (IInputScript) aClass.newInstance();
            inputScriptCache.put(key, inputScript);
        }
        return inputScript;
    }

    public IOutputScript getOutputScript(Integer definitionId, String name, String scriptText) throws IllegalAccessException, InstantiationException {
        if (StringUtils.isEmpty(scriptText)) {
            return defaultOutputScript;
        }
        String key = "s-" + definitionId + "-" + name;
        IOutputScript outputScript = outputScriptCache.getOrDefault(key, null);
        if (outputScript == null) {
            Class aClass = groovyClassLoader.parseClass(scriptText);
            outputScript = (IOutputScript) aClass.newInstance();
            outputScriptCache.put(key, outputScript);
        }
        return outputScript;
    }

    public void clear() {
        inputScriptCache = new ConcurrentHashMap<>();
        outputScriptCache = new ConcurrentHashMap<>();
    }
}
