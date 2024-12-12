package com.kieslect.file.domain.vo;

import com.kieslect.file.domain.SportKlocation;
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
public class SportKlocationVO extends SportKlocation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer totalTime;
}
