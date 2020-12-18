package com.ujm.semweb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import com.ujm.semweb.config.DumpData;
import com.ujm.semweb.service.RestService;

@ExtendWith(OutputCaptureExtension.class)
public class RestServiceTest {
	@Test
    public void testGetBikeDataRest(CapturedOutput output) {
        RestService rest=new RestService();
        rest.getBikeData();
        //       Assertions.assertThat(output.getAll()).contains("SAINT-ETIENNE");

    }
}
