package com.fred.orderreport.shared.constants;

/**
 * Centralisation des constantes métier partagées.
 *
 * <p>
 * Cette classe regroupe les principales règles métier
 * auparavant dispersées dans les calculateurs et services legacy.
 * </p>
 *
 * <p>
 * Elle améliore la lisibilité du code en remplaçant les
 * "magic numbers" par des concepts métier explicites
 * et prépare une éventuelle évolution vers un système
 * de configuration plus avancé.
 * </p>
 */
public final class BusinessConstants {

    private BusinessConstants() {
    }

    // ---------- RÈGLES DE REMISE ---------- //

    public static final double WEEKEND_DISCOUNT_BONUS = 1.05;

    public static final double MAX_DISCOUNT = 200.0;

    public static final double BASIC_DISCOUNT_RATE = 0.05;
    public static final double ADVANCED_DISCOUNT_RATE = 0.10;
    public static final double PREMIUM_DISCOUNT_RATE = 0.15;
    public static final double VIP_DISCOUNT_RATE = 0.20;

    public static final double LOYALTY_DISCOUNT_RATE = 0.10;
    public static final double PREMIUM_LOYALTY_DISCOUNT_RATE = 0.15;

    public static final double LOYALTY_DISCOUNT_THRESHOLD = 100.0;
    public static final double PREMIUM_LOYALTY_DISCOUNT_THRESHOLD = 500.0;

    public static final double MAX_LOYALTY_DISCOUNT = 50.0;
    public static final double MAX_PREMIUM_LOYALTY_DISCOUNT = 100.0;

    // ---------- RÈGLES DE FRAIS DE GESTION ---------- //

    public static final int MEDIUM_ORDER_THRESHOLD = 10;
    public static final int LARGE_ORDER_THRESHOLD = 20;

    public static final double HANDLING_FEE = 2.5;

    // ---------- RÈGLES BONUS MATIN ---------- //

    public static final int MORNING_LIMIT_HOUR = 10;

    public static final double MORNING_DISCOUNT_RATE = 0.03;

    // ---------- RÈGLES DE LIVRAISON ---------- //

    public static final double SHIPPING_LIMIT = 50.0;

    public static final double DEFAULT_SHIPPING_BASE = 5.0;
    public static final double DEFAULT_SHIPPING_PER_KG = 0.5;

    public static final double HEAVY_WEIGHT_THRESHOLD = 10.0;
    public static final double MEDIUM_WEIGHT_THRESHOLD = 5.0;

    public static final double INTERMEDIATE_WEIGHT_RATE = 0.3;

    public static final double REMOTE_ZONE_SURCHARGE = 1.2;

    public static final double FREE_SHIPPING_WEIGHT_THRESHOLD = 20.0;

    public static final double HEAVY_PACKAGE_RATE = 0.25;

    // ---------- RÈGLES DE TAXATION ---------- //

    public static final double TAX_RATE = 0.20;

    // ---------- RÈGLES DE FIDÉLITÉ ---------- //

    public static final double LOYALTY_RATIO = 0.01;
}