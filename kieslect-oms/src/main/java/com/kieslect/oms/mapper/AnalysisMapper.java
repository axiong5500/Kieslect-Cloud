package com.kieslect.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kieslect.oms.domain.Analysis;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AnalysisMapper extends BaseMapper<Analysis> {

    @Select("""
        SELECT
            DATE(FROM_UNIXTIME(activation_time)) AS activationDate,
            SUM(CASE WHEN device_id = '20000' THEN 1 ELSE 0 END) AS `20000`,
            SUM(CASE WHEN device_id = '30000' THEN 1 ELSE 0 END) AS `30000`,
            SUM(CASE WHEN device_id = '7698' THEN 1 ELSE 0 END) AS `7698`,
            SUM(CASE WHEN device_id = '7699' THEN 1 ELSE 0 END) AS `7699`,
            SUM(CASE WHEN device_id = '7751' THEN 1 ELSE 0 END) AS `7751`,
            SUM(CASE WHEN device_id = '7752' THEN 1 ELSE 0 END) AS `7752`,
            SUM(CASE WHEN device_id = '7753' THEN 1 ELSE 0 END) AS `7753`,
            SUM(CASE WHEN device_id = '7754' THEN 1 ELSE 0 END) AS `7754`,
            SUM(CASE WHEN device_id = '7770' THEN 1 ELSE 0 END) AS `7770`,
            SUM(CASE WHEN device_id = '7815' THEN 1 ELSE 0 END) AS `7815`,
            SUM(CASE WHEN device_id = '7828' THEN 1 ELSE 0 END) AS `7828`,
            (
                    SUM(CASE WHEN device_id = '20000' THEN 1 ELSE 0 END) +
                    SUM(CASE WHEN device_id = '30000' THEN 1 ELSE 0 END) +
                    SUM(CASE WHEN device_id = '7698' THEN 1 ELSE 0 END) +
                    SUM(CASE WHEN device_id = '7699' THEN 1 ELSE 0 END) +
                    SUM(CASE WHEN device_id = '7751' THEN 1 ELSE 0 END) +
                    SUM(CASE WHEN device_id = '7752' THEN 1 ELSE 0 END) +
                    SUM(CASE WHEN device_id = '7753' THEN 1 ELSE 0 END) +
                    SUM(CASE WHEN device_id = '7754' THEN 1 ELSE 0 END) +
                    SUM(CASE WHEN device_id = '7770' THEN 1 ELSE 0 END) +
                    SUM(CASE WHEN device_id = '7815' THEN 1 ELSE 0 END) +
                    SUM(CASE WHEN device_id = '7828' THEN 1 ELSE 0 END)
                ) AS totalSum
        FROM
            t_third_device_active_info
        GROUP BY
            activationDate
        ORDER BY
            activationDate DESC
    """)
    @MapKey("activationDate")
    List<Map<String, Object>> selectActivationCountByGroup();

    @Select("""
        WITH MonthlyData AS (
            SELECT
                cm.chinese_name AS countryCn,
                DATE_FORMAT(FROM_UNIXTIME(t.activation_time), '%Y-%m') AS month,
                COUNT(*) AS activation_count
            FROM
                t_third_device_active_info t
            JOIN
                country_mapping cm ON t.country = cm.country_code
            GROUP BY
                cm.chinese_name,
                month
        ),
        PivotedData AS (
            SELECT
                countryCn,
                SUM(CASE WHEN month = '2024-01' THEN activation_count ELSE 0 END) AS `01`,
                SUM(CASE WHEN month = '2024-02' THEN activation_count ELSE 0 END) AS `02`,
                SUM(CASE WHEN month = '2024-03' THEN activation_count ELSE 0 END) AS `03`,
                SUM(CASE WHEN month = '2024-04' THEN activation_count ELSE 0 END) AS `04`,
                SUM(CASE WHEN month = '2024-05' THEN activation_count ELSE 0 END) AS `05`,
                SUM(CASE WHEN month = '2024-06' THEN activation_count ELSE 0 END) AS `06`,
                SUM(CASE WHEN month = '2024-07' THEN activation_count ELSE 0 END) AS `07`,
                SUM(CASE WHEN month = '2024-08' THEN activation_count ELSE 0 END) AS `08`,
                SUM(
                    CASE\s
                        WHEN month = '2024-01' THEN activation_count
                        WHEN month = '2024-02' THEN activation_count
                        WHEN month = '2024-03' THEN activation_count
                        WHEN month = '2024-04' THEN activation_count
                        WHEN month = '2024-05' THEN activation_count
                        WHEN month = '2024-06' THEN activation_count
                        WHEN month = '2024-07' THEN activation_count
                        WHEN month = '2024-08' THEN activation_count
                        ELSE 0
                    END
                ) AS totalCount
            FROM MonthlyData
            GROUP BY countryCn
        ),
        GrowthComparison AS (
            SELECT
                countryCn,
                `01`,
                `02`,
                `03`,
                `04`,
                `05`,
                `06`,
                `07`,
                `08`,
                totalCount,
                -- 计算增速并保留小数点后两位
                ROUND(
                    CASE\s
                        WHEN `02` IS NULL OR `01` IS NULL THEN NULL
                        ELSE (`02` - `01`) / `01` * 100
                    END, 2
                ) AS growth_rate_02_vs_01,
                ROUND(
                    CASE\s
                        WHEN `03` IS NULL OR `02` IS NULL THEN NULL
                        ELSE (`03` - `02`) / `02` * 100
                    END, 2
                ) AS growth_rate_03_vs_02,
                ROUND(
                    CASE\s
                        WHEN `04` IS NULL OR `03` IS NULL THEN NULL
                        ELSE (`04` - `03`) / `03` * 100
                    END, 2
                ) AS growth_rate_04_vs_03,
                ROUND(
                    CASE\s
                        WHEN `05` IS NULL OR `04` IS NULL THEN NULL
                        ELSE (`05` - `04`) / `04` * 100
                    END, 2
                ) AS growth_rate_05_vs_04,
                ROUND(
                    CASE\s
                        WHEN `06` IS NULL OR `05` IS NULL THEN NULL
                        ELSE (`06` - `05`) / `05` * 100
                    END, 2
                ) AS growth_rate_06_vs_05,
                ROUND(
                    CASE\s
                        WHEN `07` IS NULL OR `06` IS NULL THEN NULL
                        ELSE (`07` - `06`) / `06` * 100
                    END, 2
                ) AS growth_rate_07_vs_06,
                ROUND(
                    CASE\s
                        WHEN `08` IS NULL OR `07` IS NULL THEN NULL
                        ELSE (`08` - `07`) / `07` * 100
                    END, 2
                ) AS growth_rate_08_vs_07
            FROM PivotedData
            WHERE totalCount > 0
        )
        SELECT
            countryCn,
            `01`,
            `02`,
            `03`,
            `04`,
            `05`,
            `06`,
            `07`,
            `08`,
            totalCount,
            growth_rate_02_vs_01,
            growth_rate_03_vs_02,
            growth_rate_04_vs_03,
            growth_rate_05_vs_04,
            growth_rate_06_vs_05,
            growth_rate_07_vs_06,
            growth_rate_08_vs_07
        FROM GrowthComparison
        ORDER BY totalCount DESC;
    """)
    @MapKey("totalCount")
    List<Map<String, Object>> selectCountryMonthCountByGroup();
}
