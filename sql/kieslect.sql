-- 创建新表
CREATE TABLE `t_geoname_new` AS
SELECT *
FROM `t_geoname`
WHERE `feature_code` LIKE '%ADM3%' OR `feature_code` LIKE '%ADM2%';
