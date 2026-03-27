package com.recipeshare.entity;

public enum Role {
    VIEWER,   // Chỉ xem công thức
    USER,     // Đăng công thức, cần admin duyệt
    ADMIN     // Duyệt công thức, quản lý
}
