-- 创建新表
CREATE TABLE `t_geoname` AS
SELECT * FROM `kieslect_cloud`.`t_geoname_all` WHERE `feature_code` = 'PPLC' OR `feature_code` = 'ADM1' OR `feature_code` = 'ADM2' OR `feature_code` = 'ADM3'
