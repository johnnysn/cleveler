package com.hive.apps.cleveler.service.transform;

import com.hive.apps.cleveler.cnc.transform.CompensationFuncion;
import com.hive.apps.cleveler.cnc.transform.Compensator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransformService {

    public void compensateZ(File file, CompensationFuncion function, File outputFile) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            var lines = new ArrayList<String>();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) lines.add(line);
            }

            var compensator = new Compensator(lines, function);
            var newGcode = compensator.run();
            createNewFile(newGcode, outputFile);
        }
    }

    private void createNewFile(List<String> newGcode, File outputFile) throws IOException {
        try (FileWriter writer = new FileWriter(outputFile)) {
            for(String str: newGcode) {
                writer.write(str + System.lineSeparator());
            }
        }
    }

}
