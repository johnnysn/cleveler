package com.hive.apps.cleveler.service.transform;

import com.hive.apps.cleveler.cnc.transform.CompensationFuncion;
import com.hive.apps.cleveler.cnc.transform.Compensator;

import java.io.*;
import java.util.ArrayList;

public class TransformService {

    public void compensateZ(String cncFileName, CompensationFuncion function, String outputFileName) throws IOException {
        File file = new File(cncFileName);
        BufferedReader br = new BufferedReader(new FileReader(file));

        var lines = new ArrayList<String>();
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.length() > 0) lines.add(line);
        }

        var compensator = new Compensator(lines, function);
        var newGcode = compensator.run();
        FileWriter writer = new FileWriter(outputFileName);
        for(String str: newGcode) {
            writer.write(str + System.lineSeparator());
        }
        writer.close();
    }

}
