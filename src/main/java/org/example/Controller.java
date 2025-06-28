package org.example;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Controller {
    private String modelName;
    private static Model1 model;

    public Controller(String modelName) {
        this.modelName = modelName;
        this.model = new Model1();
    }

    public void readDataFrom(String fname) {
        try (BufferedReader br = new BufferedReader(new FileReader(fname))) {
            String line;
            while ((line = br.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processLine(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length < 2) return;

        try {
            // Сначала обрабатываем строку для LL
            if ("LATA".equals(parts[0])) {
                model.setYears(parts);
                //return; // Возврат, чтобы больше ничего не обрабатывать
            }

            Field field = model.getClass().getDeclaredField(parts[0]);
            if (field.isAnnotationPresent(Bind.class)) {
                field.setAccessible(true);
                switch (field.getType().getSimpleName()) {
                    case "int":
                        break;
                    case "double[]":
                        if (model.getLL() <= 0) {
                            throw new IllegalStateException("LL must be set before initializing double arrays.");
                        }
                        double[] values = new double[model.getLL()];
                        for (int i = 1; i <= model.getLL(); i++) {
                            if (i < parts.length) {
                                values[i - 1] = Double.parseDouble(parts[i]);
                            } else if (i > 1) {
                                values[i - 1] = values[i - 2];
                            } else {
                                throw new IllegalArgumentException("No data for the first year " + parts[0]);
                            }
                        }
                        field.set(model, values);
                        break;
                    default:
                        System.out.println("Unsupported type: " + field.getType().getSimpleName());
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("Unknown variable: " + parts[0]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for variable: " + parts[0]);
        }
    }

    /*public static void runScriptFromFile(String scriptFileName) {
        try {
            String script = Files.readString(Paths.get(scriptFileName), StandardCharsets.UTF_8);
            // Чтение файла с явным указанием кодировки
            String script = Files.readString(Paths.get(scriptFileName), StandardCharsets.UTF_8);
            script = script.replace("\r\n", "\n");
            System.out.println("Script content from file: " + script);
            runScript(script);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    public static void runScript(String script) {
        Binding binding = new Binding();
        binding.setVariable("LL", model.getLL());
        binding.setVariable("KI", model.getKI());
        binding.setVariable("KS", model.getKS());
        binding.setVariable("INW", model.getINW());
        binding.setVariable("EKS", model.getEKS());
        binding.setVariable("IMP", model.getIMP());
        binding.setVariable("PKB", model.getPKB());

        GroovyShell shell = new GroovyShell(binding);
        shell.evaluate(script);

        Map<String, Object> variables = binding.getVariables();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String varName = entry.getKey();
            Object varValue = entry.getValue();

            // Check if a value is a double array
            if (varValue instanceof double[]) {
                model.setDynamicVariable(varName, (double[]) varValue);
            }
        }
    }

    public void runModel() {
        model.run();
    }

    public String getResultsAsTsv() {
        StringBuilder result = new StringBuilder();
        double[] years = model.getLATA();
        result.append("LATA");
        if (years != null) {
            for (double year : years) {
                result.append(" ").append((int) year);
            }
        }
        result.append("\n");

        String[] variableNames = {
                "twKI", "twKS", "twINW", "twEKS", "twIMP",
                "KI", "KS", "INW", "EKS", "IMP", "PKB"
        };

        Set<String> printedVariables = new HashSet<>();
        for (String variableName : variableNames) {
            if (!printedVariables.contains(variableName)) {
                result.append(variableName);
                appendArrayValues(result, variableName);
                result.append("\n");
                printedVariables.add(variableName);
            }
        }

        for (String dynamicVariableName : model.getDynamicVariables().keySet()) {
            if (!printedVariables.contains(dynamicVariableName)) {
                result.append(dynamicVariableName);
                appendArrayValues(result, dynamicVariableName);
                result.append("\n");
                printedVariables.add(dynamicVariableName);
            }
        }

        return result.toString();
    }

    private void appendArrayValues(StringBuilder result, String variableName) {
        double[] values;

        switch (variableName) {
            case "twKI":
                values = model.getTwKI();
                break;
            case "twKS":
                values = model.getTwKS();
                break;
            case "twINW":
                values = model.getTwINW();
                break;
            case "twEKS":
                values = model.getTwEKS();
                break;
            case "twIMP":
                values = model.getTwIMP();
                break;
            case "KI":
                values = model.getKI();
                break;
            case "KS":
                values = model.getKS();
                break;
            case "INW":
                values = model.getINW();
                break;
            case "EKS":
                values = model.getEKS();
                break;
            case "IMP":
                values = model.getIMP();
                break;
            case "PKB":
                values = model.getPKB();
                break;
            default:
                values = model.getDynamicVariables().get(variableName);
                break;
        }

        if (values != null) {
            for (double value : values) {
                String formattedValue;
                if (value >= 100) {
                    formattedValue = String.format("%.1f", value);
                } else if (value < 100 && value > 1) {
                    formattedValue = String.format("%.2f", value);
                } else {
                    formattedValue = String.format("%.5f", value);
                }
                result.append(" ").append(formattedValue);
            }
        }
    }
}
