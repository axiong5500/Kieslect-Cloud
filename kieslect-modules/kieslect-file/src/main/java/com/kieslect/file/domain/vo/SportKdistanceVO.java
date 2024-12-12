package com.kieslect.file.domain.vo;

import com.kieslect.file.domain.SportKdistance;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author kieslect
 * @since 2024-11-22
 */
@Data
public class SportKdistanceVO extends SportKdistance implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer totalTime;
}
