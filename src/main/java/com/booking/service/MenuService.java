package com.booking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.booking.models.Customer;
import com.booking.models.Employee;
import com.booking.models.Person;
import com.booking.models.Reservation;
import com.booking.models.Service;
import com.booking.repositories.PersonRepository;
import com.booking.repositories.ServiceRepository;

public class MenuService {
    private static List<Person> personList = PersonRepository.getAllPerson();
    private static List<Service> serviceList = ServiceRepository.getAllService();
    
    private static List<Customer> customerList = PersonRepository.getCustomerList(personList);
    private static List<Employee> employeeList = PersonRepository.getEmployeeList(personList);

    private static List<Reservation> reservationList = new ArrayList<>();
    private static Scanner input = new Scanner(System.in);

    public static void mainMenu() {
        String[] mainMenuArr = {"Show Data", "Create Reservation", "Complete/cancel reservation", "Exit"};
        String[] subMenuArr = {"Recent Reservation", "Show Customer", "Show Available Employee","Tampilkan History Reservation + Total Keuntungan", "Back to main menu"};
    
        PrintService print = new PrintService();

        int optionMainMenu;
        int optionSubMenu;

        String inputCustomerID;
        String inputEmployeeID;
        String inputServiceID;
        String anotherChoice;
        
		boolean backToMainMenu = false;
        boolean backToSubMenu = false;
        do {
            PrintService.printMenu("Main Menu", mainMenuArr);
            optionMainMenu = Integer.valueOf(input.nextLine());
            switch (optionMainMenu) {
                case 1:
                    do {
                        PrintService.printMenu("Show Data", subMenuArr);
                        optionSubMenu = Integer.valueOf(input.nextLine());
                        // Sub menu - menu 1
                        switch (optionSubMenu) {
                            case 1:
                                // panggil fitur tampilkan recent reservation
                                print.showRecentReservation(reservationList);
                                break;
                            case 2:
                                // panggil fitur tampilkan semua customer
                                print.showAllCustomer(customerList);
                                break;
                            case 3:
                                // panggil fitur tampilkan semua employee
                                print.showAllEmployee(employeeList);
                                break;
                            case 4:
                                print.showHistoryReservation(reservationList);
                                // panggil fitur tampilkan history reservation + total keuntungan
                                break;
                            case 0:
                                backToSubMenu = true;
                        }
                    } while (!backToSubMenu);
                    break;
                case 2:
                    // panggil fitur menambahkan reservation
                    boolean check = true;
                    boolean check2 = false;
                    boolean stopAddService = false;
                    boolean end = false;
                    List<Service> serviceResv = new ArrayList<>();

                    do {
                        do {
                            check = true;
                            System.out.println();
                            print.showAllCustomer(customerList);
                            System.out.println("Silahkan Masukkan Customer ID :");
                            inputCustomerID =  input.nextLine().toLowerCase();
                            for(int i=0;i<customerList.size();i++){
                                if (inputCustomerID.equals(customerList.get(i).getId().toLowerCase())) {
                                    check = false;
                                }
                            }
                            if (check) {
                                System.out.println("Customer yang dicari tidak tersedia");
                            }
                        } while (check);
                        do {
                            check = true;
                            System.out.println();
                            print.showAllEmployee(employeeList);;
                            System.out.println("Silahkan Masukkan Employee ID :");
                            inputEmployeeID =  input.nextLine().toLowerCase();
                            for(int i=0;i<employeeList.size();i++){
                                if (inputEmployeeID.equals(employeeList.get(i).getId().toLowerCase())) {
                                    check = false;
                                }
                            }
                            if (check) {
                                System.out.println("Customer yang dicari tidak tersedia");
                            }
                        } while (check);
                        do {
                            check = true;
                            System.out.println();
                            print.showAllService(serviceList);
                            System.out.println("Silahkan Masukkan Service ID :");
                            inputServiceID =  input.nextLine().toLowerCase();
                            for (Service service : serviceList) {
                                if (service.getServiceId().toLowerCase().equals(inputServiceID)) {
                                    serviceResv.add(service);
                                    check = false;
                                }
                            }
                            if (check) {
                                System.out.println("Service yang dicari tidak tersedia");
                            }
                            else{
                                do {
                                    check = true;
                                    System.out.println("Ingin pilih service yang lain (Y/T)?");
                                    anotherChoice = input.nextLine().toLowerCase();
                                    
                                    if (anotherChoice.equals("y")) {
                                        System.out.println("Silahkan Masukkan Service ID :");
                                        inputServiceID =  input.nextLine().toLowerCase();
                                        for(int i=0;i<serviceList.size();i++){
                                            if (inputServiceID.equals(serviceList.get(i).getServiceId().toLowerCase())) {
                                                for (int j = 0; j < serviceResv.size(); j++) {
                                                    if (inputServiceID.equals(serviceResv.get(j).getServiceId().toLowerCase())) {
                                                        System.out.println("Service sudah dipilih");
                                                        check2 = true;
                                                        break;
                                                    }
                                                }
                                                if (!check2) {
                                                    for (Service service : serviceList) {
                                                        if (service.getServiceId().toLowerCase().equals(inputServiceID)) {
                                                            serviceResv.add(service);
                                                            check = false;
                                                        }
                                                    }
                                                }
                                                    
                                            }
                                        }
                                        if (check && !check2) {
                                            System.out.println("Service yang dicari tidak tersedia");
                                        }
                                    }
                                    else if (anotherChoice.equals("t")) {
                                        stopAddService = true;
                                        end = true;
                                    }
                                    else{
                                        System.out.println("input tidak valid");
                                    }
                                    if (serviceResv.size() == serviceList.size()) {
                                        stopAddService = true;
                                        end = true;
                                    }
                                } while (!stopAddService);
                            }
                        } while (!check && !end);
                        System.out.println("Input reservasi ID :");
                        String reservasiID = input.nextLine();
                        Customer customerResv=null;
                        Employee employeeResv=null;

                        for (Customer customer : customerList) {
                            if (customer.getId().toLowerCase().equals(inputCustomerID)) {
                                customerResv = customer;
                            }
                        }
                        for (Employee employee : employeeList) {
                            if (employee.getId().toLowerCase().equals(inputEmployeeID)) {
                                employeeResv = employee;
                            }
                        }
                        
                        Reservation reservation = new Reservation(reservasiID, customerResv, employeeResv, serviceResv, "In Process");
                        reservationList.add(reservation);
                        System.out.println("Booking Berhasil!");
                        // System.out.printf("Total Biaya Booking : Rp.%d");
                        backToSubMenu = true;
                    }while (!backToSubMenu);
                    break;
                case 3:
                    // panggil fitur mengubah workstage menjadi finish/cancel
                    print.showRecentReservation(reservationList);
                    boolean cek = false;
                    do {
                        System.out.println("Silahkan Masukkan Reservasi ID :");
                        String resvID = input.nextLine().toLowerCase();
                        for (Reservation reservation : reservationList) {
                            if (reservation.getReservationId().toLowerCase().equals(resvID)) {
                                reservation.setWorkstage("Finish");
                                cek = true;
                            }
                        }
                        if (!cek) {
                            System.out.println("Reservasi yang dicari tidak tersedia");
                        }

                    } while (!cek);
                    break;
                case 0:
                    backToMainMenu = true;
                    break;
            }
        } while (!backToMainMenu);
		
	}
}
