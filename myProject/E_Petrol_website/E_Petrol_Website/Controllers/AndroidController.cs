using E_Petrol_Website.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace E_Petrol_Website.Controllers
{
    public class AndroidController : Controller
    {
        EPetrolDBEntities db = new EPetrolDBEntities();
        public static readonly int ADMIN = 1;
        public static readonly int EMP = 2;
        public static readonly int CUSTOMER = 3;
        private readonly int NO_USER=0;

        // GET: Android
        public ActionResult Index()
        {
            return View();
        }

        public ActionResult IssueReport()
        {
            var report = (from c in db.Customers
                          select new ReportItem
                          {
                              vehicleNumber = c.VehicleNumber,
                              balanceWithdrown = 100 - c.Balances.FirstOrDefault().TotalQuantity,
                              freeWithdrown = c.Withdraws.Where(d => d.IsFree.Equals("Yes")).ToList().Count > 0 ?
                                              c.Withdraws.Where(d => d.IsFree.Equals("Yes")).Sum(d => d.Quantity) : 0,
                              customerName = c.FullName
                          }).ToList();

            return Json(report, JsonRequestBehavior.AllowGet);
        }

        public ActionResult CustomerHome(int CustomerID)
        {/*
            if (Session[CUSTOMER] == null)
                return RedirectToAction("Login");
                */
            //get now month
            var month = DateTime.Now.Month;

            var freePetorl = db.Withdraws.Where(x => x.Date.Month == month && x.IsFree.Equals("Yes") && x.CustomerID == CustomerID).Sum(x => x.Quantity);

            var balance = db.Balances.Where(x => x.CustomerID == CustomerID).SingleOrDefault().TotalQuantity;

            var customerWithdraw = new CustomerWithdraw
            {
                FreePetrol = freePetorl,
                Balance = balance,
                FullName = db.Customers.Find(CustomerID).FullName
            };

            return Json(customerWithdraw, JsonRequestBehavior.AllowGet);
        }

        public ActionResult EmployeeHome()
        {
            var cards = db.Customers.Select(x => x.SmartCardNumber).ToList();

            return Json(cards,JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        public ActionResult WithdrawFree(long SmartCardNumber, int Amount)
        {
            var cst = db.Customers.Where(x => x.SmartCardNumber == SmartCardNumber).SingleOrDefault();
            int customerID = cst.CustomerID;

            db.Withdraws.Add(new Withdraw
            {
                CustomerID = customerID,
                Date = DateTime.Now,
                IsFree = "Yes",
                Quantity = Amount
            });
            db.SaveChanges();

            String message = "You have to pay " + 425 * Amount + " s.p ";

            return Json(new { Ok = true, Message = message }, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        public ActionResult WithdrawBalance(int Amount, long SmartCardNumber)
        {
            var date = DateTime.Now;
            var cst = db.Customers.Where(x => x.SmartCardNumber == SmartCardNumber).SingleOrDefault();
            String message = "";
            bool ok = false;

            var balance = db.Balances.Where(x => x.Customer.SmartCardNumber == SmartCardNumber).SingleOrDefault();

            var totalQuality = balance.TotalQuantity;

            var lastDay = db.Withdraws.Where(x => x.Customer.SmartCardNumber == SmartCardNumber && x.Date.Month == date.Month).ToList().Count > 0 ?
                db.Withdraws.Where(x => x.Customer.SmartCardNumber == SmartCardNumber && x.Date.Month == date.Month).Max(x => x.Date.Day) : -4;

            int days = date.Day - lastDay;
            if (Amount < 40)
            {
                if (totalQuality > 0)
                {
                    if (Amount > totalQuality)
                    {
                        message = "Your Balance is only :( " + totalQuality + " ) liters";
                    }
                    else if (days < 5)
                    {
                        message = "Your card is not opened yet, you have only free";
                    }
                    else
                    {
                        balance.TotalQuantity -= Amount;
                        db.SaveChanges();

                        db.Withdraws.Add(new Withdraw
                        {
                            CustomerID = balance.CustomerID,
                            Date = DateTime.Now,
                            IsFree = "No",
                            Quantity = Amount
                        });
                        db.SaveChanges();

                        ok = true;
                        message = "You have to pay " + 225 * Amount + " s.p ";
                    }
                }
                else
                {
                    message = "No Balance";
                }
            }
            else
            {
                message = "No more than 40 liters allowed";
            }

            return Json(new { Message = message, Ok = ok }, JsonRequestBehavior.AllowGet);
        }

        public ActionResult GetCustomerByCard(long SmartCardNumber)
        {
            var cst = db.Customers.Where(x => x.SmartCardNumber == SmartCardNumber).SingleOrDefault();
            var customer = getCustomer(cst);

            return Json(customer, JsonRequestBehavior.AllowGet);
        }

        public ActionResult GetCustomerByID(int customerID)
        {
            var cst = db.Customers.Find(customerID);
            var customer = getCustomer(cst);
            return Json(customer, JsonRequestBehavior.AllowGet);
        }

        private Customer getCustomer(Customer cst)
        {
            var customer = new Customer
            {
                FullName = cst.FullName,
                Address = cst.Address,
                CustomerID = cst.CustomerID,
                MobileNumber = cst.MobileNumber,
                Photo = cst.Photo,
                ResidencePlace = cst.ResidencePlace,
                VehicleNumber = cst.VehicleNumber,
                SmartCardNumber = cst.SmartCardNumber
            };

            return customer;
        }

        [HttpPost]
        public ActionResult CreateCustomer(String FullName, long MobileNumber, long SmartCardNumber, int VehicleNumber, String Address, String ResidencePlace)
        {
            bool ok = false;

            var existCustomer = db.Customers.Where(x => x.SmartCardNumber == SmartCardNumber).ToList();
            String message = "";
            if (existCustomer.Count == 0)
            {
                ok = true;
                var customer = new Customer
                {
                    FullName = FullName,
                    SmartCardNumber = SmartCardNumber,
                    MobileNumber = MobileNumber,
                    VehicleNumber = VehicleNumber,
                    ResidencePlace = ResidencePlace,
                    Address = Address
                };

                db.Customers.Add(customer);
                db.SaveChanges();

                db.Balances.Add(new Balance
                {
                    CustomerID = customer.CustomerID,
                    TotalQuantity = 100
                });
                db.SaveChanges();

                message = "Customer has been added successfully";
            }
            else
            {
                message = "There is another customer with the same Card Number";
            }

            return Json(new { Message = message,Ok=ok }, JsonRequestBehavior.AllowGet);
        }

        [HttpPost]
        public ActionResult Login(String Username, String Password)
        {
            String message = "";
            int role = NO_USER;
            //check admin or employee
            var user = db.Users.Where(x => x.Username.Equals(Username) && x.Password.Equals(Password)).SingleOrDefault();
            if (user != null)
            {
                if (user.Username.Equals("admin"))
                {
                    role = ADMIN;
                    return Json(new { Role = role }, JsonRequestBehavior.AllowGet);
                }
                else
                {
                    role = EMP;
                    return Json(new { Role = role }, JsonRequestBehavior.AllowGet);
                }
            }
            else
            {
                var dSmartCardNumber = Convert.ToDecimal(Password);
                //check customer
                var cst = db.Customers.Where(x => x.FullName.Equals(Username) && x.SmartCardNumber == dSmartCardNumber).SingleOrDefault();
                if (cst != null)
                {
                    role = CUSTOMER;
                    return Json(new { Role = role,CustomerID=cst.CustomerID }, JsonRequestBehavior.AllowGet);
                }
                else
                {
                    message = "Error Logging!!!";
                    return Json(new { Role = role, Message = message }, JsonRequestBehavior.AllowGet);
                }
            }
        }
    }
}