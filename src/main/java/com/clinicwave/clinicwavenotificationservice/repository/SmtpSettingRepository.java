package com.clinicwave.clinicwavenotificationservice.repository;

import com.clinicwave.clinicwavenotificationservice.domain.SmtpSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * This interface is a repository for the SmtpSetting entity.
 *
 * @author aamir on 7/8/24
 */
public interface SmtpSettingRepository extends JpaRepository<SmtpSetting, Long> {
  Optional<SmtpSetting> findByIsActiveTrue();
}
