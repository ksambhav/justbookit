package com.jbkit.catalogue.repo;

import com.jbkit.catalogue.model.NowShowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NowShowingRepo extends JpaRepository<NowShowing, String>, JpaSpecificationExecutor<NowShowing> {
}
