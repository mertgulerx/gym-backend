package com.ytu.gymbackend.config;

import com.ytu.gymbackend.model.customer.Customer;
import com.ytu.gymbackend.model.customer.CustomerHealthReport;
import com.ytu.gymbackend.model.customer.CustomerHealthReportStatus;
import com.ytu.gymbackend.model.customer.CustomerStatus;
import com.ytu.gymbackend.model.machine.Machine;
import com.ytu.gymbackend.model.machine.MachineStatus;
import com.ytu.gymbackend.model.machine.Maintenance;
import com.ytu.gymbackend.model.machine.Repair;
import com.ytu.gymbackend.model.subscription.ChargeProfile;
import com.ytu.gymbackend.model.subscription.Subscription;
import com.ytu.gymbackend.model.subscription.SubscriptionPurchase;
import com.ytu.gymbackend.model.subscription.SubscriptionStatus;
import com.ytu.gymbackend.model.user.User;
import com.ytu.gymbackend.model.user.UserRole;
import com.ytu.gymbackend.repository.ChargeProfileRepository;
import com.ytu.gymbackend.repository.CustomerHealthReportRepository;
import com.ytu.gymbackend.repository.CustomerRepository;
import com.ytu.gymbackend.repository.MachineRepository;
import com.ytu.gymbackend.repository.MaintenanceRepository;
import com.ytu.gymbackend.repository.RepairRepository;
import com.ytu.gymbackend.repository.SubscriptionPurchaseRepository;
import com.ytu.gymbackend.repository.SubscriptionRepository;
import com.ytu.gymbackend.repository.UserRepository;
import com.ytu.gymbackend.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataStarter implements ApplicationListener<ContextRefreshedEvent> {
    private final PasswordUtils passwordUtils;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final CustomerHealthReportRepository customerHealthReportRepository;
    private final ChargeProfileRepository chargeProfileRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPurchaseRepository subscriptionPurchaseRepository;
    private final MachineRepository machineRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final RepairRepository repairRepository;

    @Value("${gym.seed.demo:true}")
    private boolean seedDemo;

    public DataStarter(
            PasswordUtils passwordUtils,
            UserRepository userRepository,
            CustomerRepository customerRepository,
            CustomerHealthReportRepository customerHealthReportRepository,
            ChargeProfileRepository chargeProfileRepository,
            SubscriptionRepository subscriptionRepository,
            SubscriptionPurchaseRepository subscriptionPurchaseRepository,
            MachineRepository machineRepository,
            MaintenanceRepository maintenanceRepository,
            RepairRepository repairRepository
    ) {
        this.passwordUtils = passwordUtils;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.customerHealthReportRepository = customerHealthReportRepository;
        this.chargeProfileRepository = chargeProfileRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionPurchaseRepository = subscriptionPurchaseRepository;
        this.machineRepository = machineRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.repairRepository = repairRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!userRepository.findAll().isEmpty()) {
            return; // already seeded — skip
        }

        // Always seed the admin (matches original behaviour).
        User admin = new User();
        admin.setUserRole(UserRole.ADMIN);
        admin.setHashedPassword(passwordUtils.hashPassword("password123ytu"));
        admin.setBackupSecret(passwordUtils.hashPassword("ytu"));
        admin.setName("Ronnie");
        admin.setSurName("Coleman");
        admin = userRepository.save(admin);

        if (!seedDemo) {
            return;
        }
        seedDemoData(admin);
    }

    private void seedDemoData(User admin) {
        // ---- Extra staff ----
        createStaff("Mert", "Güler", UserRole.ADMIN);
        createStaff("Ayşe", "Demir", UserRole.CLERK);
        createStaff("Hakan", "Çelik", UserRole.CLERK);
        User repairman1 = createStaff("İsmail", "Kara", UserRole.REPAIRMAN);
        User repairman2 = createStaff("Burak", "Yıldız", UserRole.REPAIRMAN);

        // ---- Charge profiles ----
        List<ChargeProfile> profiles = new ArrayList<>();
        profiles.add(createProfile("Aylık Standart", "Standart üyelik planı", "1.00", "1500"));
        profiles.add(createProfile("Aylık Öğrenci", "Öğrenci indirimli plan (öğrenci belgesi gerekli)", "0.70", "1050"));
        profiles.add(createProfile("Yıllık Premium", "12 ay önden ödemeli, %15 indirim", "0.85", "15300"));
        profiles.add(createProfile("Sabah Kuşu", "Sadece 06:00-12:00 arası geçerli", "0.60", "900"));
        profiles.add(createProfile("Gece Vardiyası", "20:00-23:00 arası özel tarife", "0.65", "975"));
        profiles.add(createProfile("VIP Sınırsız", "Tüm hizmetler dahil VIP plan", "1.50", "2250"));

        // ---- Customers ----
        Object[][] customerSeed = {
                {"Mehmet", "Akın", "05551234567", CustomerStatus.VERIFIED, true, 200},
                {"Zeynep", "Yıldız", "05552345678", CustomerStatus.VERIFIED, true, 175},
                {"Ali", "Yılmaz", "05553456789", CustomerStatus.VERIFIED, true, 150},
                {"Fatma", "Şahin", "05554567890", CustomerStatus.PENDING, false, 130},
                {"Hasan", "Öztürk", "05555678901", CustomerStatus.VERIFIED, true, 120},
                {"Elif", "Kaya", "05556789012", CustomerStatus.PENDING, false, 110},
                {"Mustafa", "Aydın", "05557890123", CustomerStatus.VERIFIED, true, 95},
                {"Ayşe", "Çetin", "05558901234", CustomerStatus.PENDING, false, 80},
                {"Emre", "Polat", "05559012345", CustomerStatus.VERIFIED, true, 70},
                {"Selin", "Arslan", "05550123456", CustomerStatus.BLACKLIST, false, 60},
                {"Cem", "Erdoğan", "05554440011", CustomerStatus.VERIFIED, true, 50},
                {"Deniz", "Korkmaz", "05554440022", CustomerStatus.VERIFIED, true, 45},
                {"Pelin", "Avcı", "05554440033", CustomerStatus.PENDING, false, 30},
                {"Onur", "Ergin", "05554440044", CustomerStatus.VERIFIED, true, 28},
                {"Berk", "Türk", "05554440055", CustomerStatus.VERIFIED, true, 22},
                {"Sena", "Aksoy", "05554440066", CustomerStatus.VERIFIED, true, 18},
                {"Doruk", "Kurt", "05554440077", CustomerStatus.PENDING, false, 14},
                {"İrem", "Yalçın", "05554440088", CustomerStatus.VERIFIED, true, 12},
                {"Tolga", "Bakır", "05554440099", CustomerStatus.VERIFIED, true, 9},
                {"Ece", "Doğan", "05554440100", CustomerStatus.PENDING, false, 5},
                {"Kaan", "Şimşek", "05554440111", CustomerStatus.VERIFIED, true, 3},
                {"Lara", "Erol", "05554440122", CustomerStatus.VERIFIED, true, 2},
        };

        int profileIdx = 0;
        for (Object[] s : customerSeed) {
            String name = (String) s[0];
            String surName = (String) s[1];
            String phone = (String) s[2];
            CustomerStatus status = (CustomerStatus) s[3];
            boolean activeSubscriber = (Boolean) s[4];
            int daysAgo = (Integer) s[5];

            Customer customer = new Customer();
            customer.setName(name);
            customer.setSurName(surName);
            customer.setPhoneNumber(phone);
            customer.setCustomerStatus(status);
            customer = customerRepository.save(customer);

            // Health report — VERIFIED + PENDING customers have a report.
            if (status == CustomerStatus.VERIFIED) {
                CustomerHealthReport report = new CustomerHealthReport();
                report.setCustomer(customer);
                report.setFileName("saglik_" + customer.getId() + ".pdf");
                report.setPdfData(("MOCK_PDF_" + customer.getId()).getBytes());
                report.setCustomerHealthReportStatus(CustomerHealthReportStatus.VERIFIED);
                report.setRevisionDate(LocalDate.now().minusDays(Math.max(1, daysAgo - 2)));
                report.setEndDate(LocalDate.now().plusDays(180 - Math.min(daysAgo, 100)));
                customerHealthReportRepository.save(report);
            } else if (status == CustomerStatus.PENDING) {
                CustomerHealthReport report = new CustomerHealthReport();
                report.setCustomer(customer);
                report.setFileName("saglik_" + customer.getId() + ".pdf");
                report.setPdfData(("MOCK_PDF_" + customer.getId()).getBytes());
                report.setCustomerHealthReportStatus(CustomerHealthReportStatus.PENDING);
                report.setRevisionDate(LocalDate.now().minusDays(2));
                report.setEndDate(LocalDate.now().plusDays(180));
                customerHealthReportRepository.save(report);
            }

            // Subscription + (optionally) purchase
            Subscription subscription = new Subscription();
            subscription.setCustomer(customer);
            subscription.setStatus(activeSubscriber ? SubscriptionStatus.ACTIVE : SubscriptionStatus.NO_PURCHASE_YET);
            if (activeSubscriber) {
                subscription.setEndDate(LocalDate.now().plusDays(30));
            }
            subscription = subscriptionRepository.save(subscription);

            if (activeSubscriber) {
                ChargeProfile profile = profiles.get(profileIdx++ % profiles.size());
                int monthPeriod = profile.getTitle().contains("Yıllık") ? 12 : 1;
                boolean timeLimited = profile.getTitle().contains("Sabah") || profile.getTitle().contains("Gece");

                SubscriptionPurchase purchase = new SubscriptionPurchase();
                purchase.setSubscription(subscription);
                purchase.setIsCompleted(false);
                purchase.setTitle(profile.getTitle());
                purchase.setSubscriptionDays(30);
                purchase.setSubscriptionMonthPeriod(monthPeriod);
                purchase.setChargeRate(profile.getChargeRate());
                purchase.setChargeCost(profile.getChargeCost());
                purchase.setMonthlyCost(profile.getChargeCost().multiply(profile.getChargeRate()));
                purchase.setTotalCost(profile.getChargeCost()
                        .multiply(profile.getChargeRate())
                        .multiply(BigDecimal.valueOf(monthPeriod)));
                purchase.setIsTimeLimited(timeLimited);
                if (timeLimited) {
                    purchase.setStartHour(profile.getTitle().contains("Sabah") ? 6 : 20);
                    purchase.setEndHour(profile.getTitle().contains("Sabah") ? 12 : 23);
                }
                subscriptionPurchaseRepository.save(purchase);
            }
        }

        // ---- Machines ----
        Object[][] machineSeed = {
                {"Koşu Bandı M-04", MachineStatus.AVAILABLE, 6, 80},
                {"Eliptik Bisiklet E-02", MachineStatus.AVAILABLE, 6, 60},
                {"Kondisyon Bisikleti C-07", MachineStatus.AVAILABLE, 4, 145},
                {"Smith Machine S-01", MachineStatus.AVAILABLE, 12, 200},
                {"Leg Press L-03", MachineStatus.AVAILABLE, 6, 175},
                {"Lat Pulldown L-05", MachineStatus.AVAILABLE, 6, 30},
                {"Pec Deck P-02", MachineStatus.ON_REPAIR_SERVICE, 6, 250},
                {"Cable Crossover X-01", MachineStatus.AVAILABLE, 12, 90},
                {"Dumbbell Rack D-01", MachineStatus.AVAILABLE, 24, 365},
                {"Squat Rack R-02", MachineStatus.AVAILABLE, 12, 120},
                {"Rowing Machine W-01", MachineStatus.AVAILABLE, 4, 130},
                {"Hack Squat H-01", MachineStatus.ON_REPAIR_SERVICE, 6, 220},
        };

        List<Machine> machines = new ArrayList<>();
        for (Object[] s : machineSeed) {
            Machine machine = new Machine();
            machine.setName((String) s[0]);
            machine.setMachineStatus((MachineStatus) s[1]);
            machine.setMaintenanceMonthlyPeriod((Integer) s[2]);
            machine.setLastMaintenanceDate(LocalDate.now().minusDays((Integer) s[3]));
            machines.add(machineRepository.save(machine));
        }

        // ---- Maintenance log ----
        String[] maintNotes = {
                "Yağlama ve kayış kontrolü yapıldı",
                "Motor temizliği ve filtre değişimi",
                "Elektronik panel kalibrasyonu",
                "Cıvatalar sıkıldı, yatak değişimi",
                "Kablo değişimi ve gres yağlama",
                "Ekran tamiri ve hız kalibrasyonu",
                "Genel temizlik ve gres",
                "Vibrasyon emici tamiri",
        };
        for (Machine m : machines) {
            int count = 1 + (int) (Math.random() * 3);
            for (int i = 0; i < count; i++) {
                Maintenance maintenance = new Maintenance();
                maintenance.setMachine(m);
                maintenance.setMaintainer(repairman1);
                maintenance.setCost(BigDecimal.valueOf(200 + (long) (Math.random() * 1500)));
                maintenance.setInfo(maintNotes[(int) (Math.random() * maintNotes.length)]);
                maintenanceRepository.save(maintenance);
            }
        }

        // ---- Repairs (a few historical + 2 open) ----
        Object[][] repairSeed = {
                {"Motor arızası, motor değişimi gerekti", 7, "7500", true, 60, 2},
                {"Ekran arızalı, yedek parça beklendi", 5, "1800", true, 25, 5},
                {"Pec Deck pim sistemi tamiri", 4, "2500", false, 3, 6},
                {"Hack Squat ray sistemi yenileme", 10, "4200", false, 8, 11},
        };
        for (Object[] s : repairSeed) {
            int idx = (Integer) s[5];
            if (idx >= machines.size()) continue;

            Repair repair = new Repair();
            repair.setMachine(machines.get(idx));
            repair.setMaintainer(repairman2);
            repair.setCost(new BigDecimal((String) s[2]));
            repair.setInfo((String) s[0]);
            repair.setEstimatedReturnDays((Integer) s[1]);
            repair.setIsCompleted((Boolean) s[3]);
            if ((Boolean) s[3]) {
                repair.setCompleteDate(LocalDate.now().minusDays(Math.max(0, (Integer) s[4] - (Integer) s[1])));
            }
            repairRepository.save(repair);
        }
    }

    private User createStaff(String name, String surName, UserRole role) {
        User user = new User();
        user.setUserRole(role);
        user.setHashedPassword(passwordUtils.hashPassword("Demo123!"));
        user.setBackupSecret(passwordUtils.hashPassword("demo"));
        user.setName(name);
        user.setSurName(surName);
        return userRepository.save(user);
    }

    private ChargeProfile createProfile(String title, String info, String rate, String cost) {
        ChargeProfile profile = new ChargeProfile();
        profile.setTitle(title);
        profile.setInfo(info);
        profile.setChargeRate(new BigDecimal(rate));
        profile.setChargeCost(new BigDecimal(cost));
        return chargeProfileRepository.save(profile);
    }
}
