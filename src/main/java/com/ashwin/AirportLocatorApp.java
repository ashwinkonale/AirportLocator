package com.ashwin;

import com.ashwin.flink.AirportLocateMapFunction;
import com.ashwin.utils.AirportDataLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.core.fs.FileSystem;

import java.io.File;
import java.io.IOException;

public class AirportLocatorApp {
    private AppConfig appConfig;

    public AirportLocatorApp(String configurationFile) throws IOException {
        loadConfiguration(configurationFile);
    }

    private void loadConfiguration(String filename) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        appConfig = mapper.readValue(new File(filename), AppConfig.class);
    }

    public static void main(String[] args) throws Exception {
        if(args.length == 0){
            System.out.println("Required: config.yml");
            System.exit(0);
        }
        String configurationFile = args[0];
        AirportLocatorApp app = new AirportLocatorApp(configurationFile);

        AppConfig appConfig = app.appConfig;
        new AirportDataLoader(appConfig).load();

        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSet<Tuple3<String, Double, Double>> inputData = env.readCsvFile(appConfig.usersDataFilePath)
                .ignoreFirstLine().types(String.class, Double.class, Double.class);

        DataSet<Tuple2<String, String>> set = inputData.map(new AirportLocateMapFunction(appConfig.redisConfig));

        // Can increase parallelism for distributed writing.
        set.writeAsCsv("file://" + appConfig.outputFilePath, FileSystem.WriteMode.OVERWRITE).setParallelism(1);
        env.execute();
    }
}
