package com.jbkit.catalogue.api;

import com.jbkit.catalogue.model.NowShowing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

//TODO Use test containers for postgres
@SpringBootTest
class NowShowingInfoProviderImplTest {

    @Autowired
    private NowShowingInfoProviderImpl nowShowingInfoProvider;

    @Test
    void find() {
        Assertions.assertNotNull(nowShowingInfoProvider);
        //
        Page<NowShowing> nowShowings = nowShowingInfoProvider.find(null, null, null, null);
        Assertions.assertNotNull(nowShowings);
        nowShowings = nowShowingInfoProvider.find("Jaipur", null, null, null);
        Assertions.assertNotNull(nowShowings);
    }
}