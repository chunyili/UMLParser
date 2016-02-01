package edu.sjsu.chunyi.util;

import java.io.File;
import java.util.List;

/**
 * Created by jilongsun on 10/21/15.
 */

public class ReadFiles {


        public static List<String> listFilesForFolder(final File folder) {


            for (final File fileEntry : folder.listFiles()) {

                if (fileEntry.isDirectory()) {
                    listFilesForFolder(fileEntry);

                } else {
                    String file = fileEntry.getName();
                    if (file != null) {
                        MapAndSet.classes.add(file);
                    }

                }

            }

            return MapAndSet.classes;
        }

}
