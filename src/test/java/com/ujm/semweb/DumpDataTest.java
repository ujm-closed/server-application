package com.ujm.semweb;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import com.ujm.semweb.config.DumpData;

@ExtendWith(OutputCaptureExtension.class)
public class DumpDataTest {
	@Test
    public void testDumpStations(CapturedOutput output) {
        DumpData dump=new DumpData();
//        dump.dumpCitiesGraph();

//        dump.dumpTrainStationGraph();
        dump.dumpHospitalGraph();
//       Assertions.assertThat(output.getAll()).contains("SAINT-ETIENNE");
    }
}
