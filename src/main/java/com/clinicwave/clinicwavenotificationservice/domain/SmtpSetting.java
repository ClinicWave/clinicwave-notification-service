package com.clinicwave.clinicwavenotificationservice.domain;

import com.clinicwave.clinicwavenotificationservice.audit.Audit;
import jakarta.persistence.*;
import lombok.*;

/**
 * This class represents the SmtpSetting entity.
 *
 * @author aamir on 7/8/24
 */
@Entity
@Table(name = "SmtpSetting")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SmtpSetting extends Audit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String host;

  @Column(nullable = false)
  private Integer port;

  @Column(nullable = false)
  private String fromAddress;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private Boolean auth = true;

  @Column(nullable = false)
  private Boolean starttlsEnable = true;

  @Column(nullable = false)
  private Boolean isActive = true;
}
