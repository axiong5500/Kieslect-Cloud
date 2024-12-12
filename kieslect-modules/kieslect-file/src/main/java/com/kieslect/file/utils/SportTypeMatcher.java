package com.kieslect.file.utils;

import com.kieslect.file.enums.KieslectSportTypeEnum;
import com.kieslect.file.enums.StravaActivityTypeEnum;

import java.util.HashMap;
import java.util.Map;

public class SportTypeMatcher {
    // 创建一个映射表来存储 KieslectSportType 和 StravaActivityTypeEnum 的对应关系
    private static final Map<KieslectSportTypeEnum, StravaActivityTypeEnum> sportTypeToStravaMap = new HashMap<>();

    static {
        // 初始化映射表
        sportTypeToStravaMap.put(KieslectSportTypeEnum.INDOOR_RUN, StravaActivityTypeEnum.VIRTUAL_RUN); // 室内跑步 -> 虚拟跑步
        sportTypeToStravaMap.put(KieslectSportTypeEnum.OUTDOOR_RUN, StravaActivityTypeEnum.RUN); // 户外跑步 -> 跑步
        sportTypeToStravaMap.put(KieslectSportTypeEnum.INDOOR_CYCLING, StravaActivityTypeEnum.VIRTUAL_RIDE); // 室内骑行 -> 虚拟骑行
        sportTypeToStravaMap.put(KieslectSportTypeEnum.OUTDOOR_CYCLING, StravaActivityTypeEnum.RIDE); // 户外骑行 -> 骑行
        sportTypeToStravaMap.put(KieslectSportTypeEnum.POOL_SWIMMING, StravaActivityTypeEnum.SWIM); // 泳池游泳 -> 游泳
        sportTypeToStravaMap.put(KieslectSportTypeEnum.HIKING, StravaActivityTypeEnum.HIKE); // 徒步 -> 远足
        sportTypeToStravaMap.put(KieslectSportTypeEnum.STRENGTH_TRAINING, StravaActivityTypeEnum.WORKOUT); // 力量训练 -> 健身训练
        sportTypeToStravaMap.put(KieslectSportTypeEnum.YOGA, StravaActivityTypeEnum.YOGA); // 瑜伽 -> 瑜伽
        sportTypeToStravaMap.put(KieslectSportTypeEnum.ROWING, StravaActivityTypeEnum.ROWING); // 划船 -> 划船
        sportTypeToStravaMap.put(KieslectSportTypeEnum.SKIING, StravaActivityTypeEnum.ROLLER_SKI); // 滑雪 -> 滑轮滑雪
        sportTypeToStravaMap.put(KieslectSportTypeEnum.SNOWBOARDING, StravaActivityTypeEnum.SNOWBOARD); // 单板滑雪 -> 单板滑雪
        sportTypeToStravaMap.put(KieslectSportTypeEnum.TABLE_TENNIS, StravaActivityTypeEnum.TABLE_TENNIS); // 乒乓球 -> 乒乓球
        sportTypeToStravaMap.put(KieslectSportTypeEnum.TENNIS, StravaActivityTypeEnum.TENNIS); // 网球 -> 网球
        sportTypeToStravaMap.put(KieslectSportTypeEnum.SURFING, StravaActivityTypeEnum.SURFING); // 冲浪 -> 冲浪
        sportTypeToStravaMap.put(KieslectSportTypeEnum.BADMINTON, StravaActivityTypeEnum.BADMINTON); // 羽毛球 -> 羽毛球
        sportTypeToStravaMap.put(KieslectSportTypeEnum.PICKLEBALL, StravaActivityTypeEnum.PICKLEBALL); // 匹克球 -> 匹克球
        sportTypeToStravaMap.put(KieslectSportTypeEnum.PILATES, StravaActivityTypeEnum.PILATES); // 普拉提 -> 普拉提
        sportTypeToStravaMap.put(KieslectSportTypeEnum.GOLF, StravaActivityTypeEnum.GOLF); // 高尔夫 -> 高尔夫
        sportTypeToStravaMap.put(KieslectSportTypeEnum.ROCK_CLIMBING, StravaActivityTypeEnum.ROCK_CLIMBING); // 攀岩 -> 攀岩
        sportTypeToStravaMap.put(KieslectSportTypeEnum.SOCCER, StravaActivityTypeEnum.SOCCER); // 足球 -> 足球
        sportTypeToStravaMap.put(KieslectSportTypeEnum.SQUASH, StravaActivityTypeEnum.SQUASH); // 壁球 -> 壁球
        sportTypeToStravaMap.put(KieslectSportTypeEnum.WEIGHT_TRAINING, StravaActivityTypeEnum.WEIGHT_TRAINING); // 举重训练 -> 举重训练
        sportTypeToStravaMap.put(KieslectSportTypeEnum.RACE_WALKING, StravaActivityTypeEnum.RUN); // 竞走 -> 跑步
    }
    public static StravaActivityTypeEnum getIndoorStravaActivityType(KieslectSportTypeEnum sport) {
        if (sportTypeToStravaMap.getOrDefault(sport, StravaActivityTypeEnum.WORKOUT).getCategory() == 2) {
            return StravaActivityTypeEnum.WORKOUT;
        }
        return sportTypeToStravaMap.getOrDefault(sport, StravaActivityTypeEnum.WORKOUT);
    }

    public static StravaActivityTypeEnum getOutdoorStravaActivityType(KieslectSportTypeEnum sport) {
        if (sportTypeToStravaMap.getOrDefault(sport, StravaActivityTypeEnum.RUN).getCategory() == 1) {
            return StravaActivityTypeEnum.RUN;
        }
        return sportTypeToStravaMap.getOrDefault(sport, StravaActivityTypeEnum.RUN);
    }
}
