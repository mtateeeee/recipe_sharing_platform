package com.recipeshare.entity;

public enum RecipeStatus {
    PENDING,   // Chờ admin duyệt
    APPROVED,  // Đã duyệt, hiển thị công khai
    REJECTED   // Bị từ chối
}
