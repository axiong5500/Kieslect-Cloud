package com.kieslect.file.enums;

import java.util.ArrayList;
import java.util.List;

public enum StravaActivityTypeEnum {
    ALPINE_SKI(0, "高山滑雪", "AlpineSki", 2),
    BACKCOUNTRY_SKI(1, "越野滑雪", "BackcountrySki", 2),
    BADMINTON(2, "羽毛球", "Badminton", 1),
    CANOEING(3, "皮划艇", "Canoeing", 2),
    CROSSFIT(4, "Crossfit（高强度训练）", "Crossfit", 1),
    EBIKE_RIDE(5, "电动自行车骑行", "EBikeRide", 2),
    ELLIPTICAL(6, "椭圆机", "Elliptical", 1),
    EMOUNTAIN_BIKE_RIDE(7, "电动山地自行车骑行", "EMountainBikeRide", 2),
    GOLF(8, "高尔夫", "Golf", 2),
    GRAVEL_RIDE(9, "砂石路骑行", "GravelRide", 2),
    HANDCYCLE(10, "手动自行车", "Handcycle", 2),
    HIGH_INTENSITY_INTERVAL_TRAINING(11, "高强度间歇训练", "HighIntensityIntervalTraining", 1),
    HIKE(12, "远足", "Hike", 2),
    ICE_SKATE(13, "冰上滑冰", "IceSkate", 1),
    INLINE_SKATE(14, "溜冰", "InlineSkate", 2),
    KAYAKING(15, "皮艇", "Kayaking", 2),
    KITESURF(16, "风筝冲浪", "Kitesurf", 2),
    MOUNTAIN_BIKE_RIDE(17, "山地自行车骑行", "MountainBikeRide", 2),
    NORDIC_SKI(18, "北欧滑雪", "NordicSki", 2),
    PICKLEBALL(19, "匹克球", "Pickleball", 1),
    PILATES(20, "普拉提", "Pilates", 1),
    RACQUETBALL(21, "壁球", "Racquetball", 1),
    RIDE(22, "骑行", "Ride", 2),
    ROCK_CLIMBING(23, "攀岩", "RockClimbing", 2),
    ROLLER_SKI(24, "滑轮滑雪", "RollerSki", 2),
    ROWING(25, "划船", "Rowing", 2),
    RUN(26, "跑步", "Run", 2),
    SAIL(27, "帆船", "Sail", 2),
    SKATEBOARD(28, "滑板", "Skateboard", 2),
    SNOWBOARD(29, "单板滑雪", "Snowboard", 2),
    SNOWSHOE(30, "雪鞋行走", "Snowshoe", 2),
    SOCCER(31, "足球", "Soccer", 2),
    SQUASH(32, "壁球", "Squash", 1),
    STAIR_STEPPER(33, "踏步机", "StairStepper", 1),
    STAND_UP_PADDLE(34, "立式划水板", "StandUpPaddle", 2),
    SURFING(35, "冲浪", "Surfing", 2),
    SWIM(36, "游泳", "Swim", 1),
    TABLE_TENNIS(37, "乒乓球", "TableTennis", 1),
    TENNIS(38, "网球", "Tennis", 2),
    TRAIL_RUN(39, "越野跑", "TrailRun", 2),
    VELOMOBILE(40, "人力三轮车", "Velomobile", 2),
    VIRTUAL_RIDE(41, "虚拟骑行", "VirtualRide", 1),
    VIRTUAL_ROW(42, "虚拟划船", "VirtualRow", 1),
    VIRTUAL_RUN(43, "虚拟跑步", "VirtualRun", 1),
    WALK(44, "步行", "Walk", 2),
    WEIGHT_TRAINING(45, "举重训练", "WeightTraining", 1),
    WHEELCHAIR(46, "轮椅运动", "Wheelchair", 1),
    WINDSURF(47, "风帆冲浪", "Windsurf", 2),
    WORKOUT(48, "健身训练", "Workout", 1),
    YOGA(49, "瑜伽", "Yoga", 1);

    private final int code;
    private final String description; // 中文描述
    private final String englishDescription; // 英文描述
    private final int category; // 1 = 室内, 2 = 室外

    // 构造函数
    StravaActivityTypeEnum(int code, String description, String englishDescription, int category) {
        this.code = code;
        this.description = description;
        this.englishDescription = englishDescription;
        this.category = category;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getEnglishDescription() {
        return englishDescription;
    }

    public int getCategory() {
        return category;
    }

    // 根据category获取所有运动类型
    public static List<StravaActivityTypeEnum> getActivitiesByCategory(int category) {
        List<StravaActivityTypeEnum> activities = new ArrayList<>();
        for (StravaActivityTypeEnum type : values()) {
            if (type.getCategory() == category) {
                activities.add(type);
            }
        }
        return activities;
    }

    // 根据code获取枚举
    public static StravaActivityTypeEnum fromCode(int code) {
        for (StravaActivityTypeEnum type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null; // 返回null，表示不存在该类型
    }

    // 根据description获取枚举
    public static StravaActivityTypeEnum fromDescription(String description) {
        for (StravaActivityTypeEnum type : values()) {
            if (type.getDescription().equalsIgnoreCase(description)) {
                return type;
            }
        }
        return null; // 返回null，表示不存在该描述
    }

    @Override
    public String toString() {
        return description; // 默认返回中文描述
    }
}



