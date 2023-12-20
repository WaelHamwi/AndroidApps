using E_Petrol_Website.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace E_Petrol_Website.Controllers
{
    public class HomeController : Controller
    {
        EPetrolDBEntities db = new EPetrolDBEntities();
        public static readonly String ADMIN = "admin";
        public static readonly String EMP = "emp";
        public static readonly String CUSTOMER = "customer";
        public ActionResult Index()
        {
            return View();
        }

        public ActionResult About()
        {
            ViewBag.Message = "Your application description page.";

            return View();
        }

        public ActionResult Contact()
        {
            ViewBag.Message = "Your contact page.";

            return View();
        }


        public ActionResult ChangeName()
        {
            var dName = db.CompanyNames.Find(1);

            return View(dName);
        }
        public ActionResult AllCustomers()
        {
            var customers = db.Customers.ToList();

            return View(customers);
        }

        public ActionResult Delete(int id)
        {
            var customer = db.Customers.Find(id);
            db.Customers.Remove(customer);
            db.SaveChanges();

            return RedirectToAction("AllCustomers");
        }
        [HttpPost]
      public ActionResult ChangeName(String name)
        {
            var _dName = db.CompanyNames.Find(1);

            _dName.Name = name;
            db.SaveChanges();

            return RedirectToAction("AdminHome");
        }

        public ActionResult CustomerHome(int customerID)
        {/*
            if (Session[CUSTOMER] == null)
                return RedirectToAction("Login");
                */
            //get now month
            var month = DateTime.Now.Month;

            var name = db.Customers.Find(customerID).FullName;

            var freePetorl = db.Withdraws.Where(x => x.Date.Month == month && x.IsFree.Equals("Yes") && x.CustomerID==customerID).Count()>0?
                db.Withdraws.Where(x => x.Date.Month == month && x.IsFree.Equals("Yes") && x.CustomerID == customerID).Sum(x => x.Quantity):0;

            var balance = db.Balances.Where(x => x.CustomerID == customerID).SingleOrDefault().TotalQuantity;

            ViewBag.FreePetrol = freePetorl;
            ViewBag.Balance = balance;
            ViewBag.Name = name;

            return View();
        }

        public ActionResult EmployeeHome()
        {
            /*
            if (Session[EMP] == null)
                return RedirectToAction("Login");
                */
            var cards = db.Customers.Select(x => x.SmartCardNumber).ToList();

            ViewBag.SmartCardNumbers = cards;

            return View();
        }

        [HttpPost]
        public ActionResult WithdrawFree(long smartCardNumber,String amount)
        {
            try {
                int iamount = Int32.Parse(amount);
                if (iamount < 1)
                {
                    ViewBag.Message = "Error Fields";
                    return RedirectToAction("WithdrawFree", new { smartCardNumber = smartCardNumber });
                }

                var cst = db.Customers.Where(x => x.SmartCardNumber == smartCardNumber).SingleOrDefault();
                int customerID = cst.CustomerID;

                db.Withdraws.Add(new Withdraw
                {
                    CustomerID = customerID,
                    Date = DateTime.Now,
                    IsFree = "Yes",
                    Quantity = iamount
                });
                db.SaveChanges();

                ViewBag.SmartCardNumber = smartCardNumber;
                ViewBag.Message = "You have to pay " + 425 * iamount + " s.p ";
                return View(cst);
            }
            catch
            {
                ViewBag.Message = "Error Fields";
                return RedirectToAction("WithdrawFree", new { smartCardNumber = smartCardNumber });
            }
            
        }

        public ActionResult WithdrawFree(long smartCardNumber)
        {
            var cst = db.Customers.Where(x => x.SmartCardNumber == smartCardNumber).SingleOrDefault();
            ViewBag.Photo = cst.Photo;
            ViewBag.SmartCardNumber = smartCardNumber;
            ViewBag.Customer = cst;

            return View(cst);
        }

        public ActionResult WithdrawBalance(long smartCardNumber)
        {
            var cst = db.Customers.Where(x => x.SmartCardNumber == smartCardNumber).SingleOrDefault();
            ViewBag.Photo = cst.Photo;
            ViewBag.SmartCardNumber = smartCardNumber;
            ViewBag.Customer = cst;

            return View(cst);
        }

        [HttpPost]
        public ActionResult WithdrawBalance(String amount,long smartCardNumber)
        {
            int iamount = 0;
            try
            {
                iamount = Int32.Parse(amount);

                if (iamount < 1)
                {
                    ViewBag.Message = "Error Fields";
                    return RedirectToAction("WithdrawBalance", new { smartCardNumber = smartCardNumber });
                }

                var date = DateTime.Now;
            var cst = db.Customers.Where(x => x.SmartCardNumber == smartCardNumber).SingleOrDefault();

            var balance = db.Balances.Where(x => x.Customer.SmartCardNumber == smartCardNumber).SingleOrDefault();

            var totalQuality=balance.TotalQuantity;

            var lastDay = db.Withdraws.Where(x => x.Customer.SmartCardNumber == smartCardNumber && x.Date.Month == date.Month).ToList().Count>0?
                db.Withdraws.Where(x => x.Customer.SmartCardNumber == smartCardNumber && x.Date.Month == date.Month).Max(x => x.Date.Day):-4;

            int days = date.Day - lastDay;
            if (iamount < 40) {
                if (totalQuality > 0)
                {
                    if (iamount > totalQuality)
                    {
                        ViewBag.Message = "Your Balance is only :( " + totalQuality + " ) liters";
                    }
                    else if (days < 5)
                    {
                        ViewBag.Message = "Your card is not opened yet, you have only free";
                    }
                    else
                    {
                        balance.TotalQuantity -= iamount;
                        db.SaveChanges();

                        db.Withdraws.Add(new Withdraw
                        {
                            CustomerID = balance.CustomerID,
                            Date = DateTime.Now,
                            IsFree = "No",
                            Quantity = iamount
                        });
                        db.SaveChanges();

                        ViewBag.Message = "You have to pay " + 225 * iamount + " s.p ";
                    }
                }
                else
                {
                    ViewBag.Message = "No Balance";
                }
            }
            else
            {
                ViewBag.Message = "No more than 40 liters allowed";
            }
                ViewBag.smartCardNumber = smartCardNumber;

                return View(cst);
            }
            catch
            {
                ViewBag.Message = "Error Fields";
                return RedirectToAction("WithdrawBalance", new { smartCardNumber = smartCardNumber });
            }

          
        }

        public ActionResult CreateCustomer()
        {
            return View();
        }
        public ActionResult AdminHome()
        {/*
            if (Session[ADMIN] == null)
                return RedirectToAction("Login");
                */
            return View();
        }

        public ActionResult IssueReport()
        {
            var report = (from c in db.Customers
                     select new ReportItem
                     {
                         vehicleNumber = c.VehicleNumber,
                         balanceWithdrown = 100 - c.Balances.FirstOrDefault().TotalQuantity,
                         freeWithdrown = c.Withdraws.Where(d => d.IsFree.Equals("Yes")).ToList().Count>0?
                                         c.Withdraws.Where(d => d.IsFree.Equals("Yes")).Sum(d => d.Quantity):0,
                         customerName = c.FullName
                     }).ToList();

            return View(report);
        }

        [HttpPost]
        public ActionResult CreateCustomer(String fullName,String mobileNumber,HttpPostedFileBase photo,String smartCardNumber,String vehicleNumber,String address,String residencePlace)
        {
            try {
                long cardNumber = long.Parse(smartCardNumber);
                int mobile = Int32.Parse(mobileNumber);
                int vehicle = Int32.Parse(vehicleNumber);

                var existCustomer = db.Customers.Where(x => x.SmartCardNumber == cardNumber).ToList();
                if (existCustomer.Count == 0)
                {
                    string filePathInDB = "";
                    var customer = new Customer
                    {
                        FullName = fullName,
                        SmartCardNumber = cardNumber,
                        MobileNumber = mobile,
                        VehicleNumber = vehicle,
                        ResidencePlace = residencePlace,
                        Address = address
                    };

                    if (photo != null && photo.ContentLength > 0)
                    {
                        string fileName = System.IO.Path.GetFileName(photo.FileName);
                        string filePath = "~/Photos/" + fileName;
                        filePathInDB = "Photos/" + fileName;
                        photo.SaveAs(Server.MapPath(filePath));
                        Response.Write(filePathInDB);
                        customer.Photo = filePathInDB;
                    }

                    db.Customers.Add(customer);
                    db.SaveChanges();

                    db.Balances.Add(new Balance
                    {
                        CustomerID = customer.CustomerID,
                        TotalQuantity = 100
                    });
                    db.SaveChanges();

                    ViewBag.Message = "Customer has been added successfully";
                }
                else
                {
                    ViewBag.Message = "There is another customer with the same Card Number";
                }
            }
            catch
            {
                ViewBag.Message = "Error Fields";
            }
            return View();
        }

       

        public ActionResult Login()
        {
            if (Session[ADMIN] != null)
                return RedirectToAction("AdminHome");
            if (Session[EMP] != null)
                RedirectToAction("EmployeeHome");
            if (Session[CUSTOMER] != null)
                RedirectToAction("CustomerHome");
            return View();
        }

        [HttpPost]
       public ActionResult Login(String username,String password)
        {
            Session["login"] = 0;
            //check admin or employee
            var user = db.Users.Where(x => x.Username.Equals(username) && x.Password.Equals(password)).SingleOrDefault();
            if (user != null)
            {
                if (user.Username.Equals("admin"))
                {
                    Session["login"] = 1;
                    Session["role"] = 1;
                    return RedirectToAction("AdminHome");
                }
                else
                {

                    Session["login"] = 1;
                    Session["role"] = 2;
                    return RedirectToAction("EmployeeHome");
                }
            }
            else
            {
                try {
                    var dSmartCardNumber = Convert.ToDecimal(password);
                    //check customer
                    var cst = db.Customers.Where(x => x.FullName.Equals(username) && x.SmartCardNumber == dSmartCardNumber).SingleOrDefault();
                    if (cst != null)
                    {
                        Session["role"] = 3;
                        Session["name"] = cst.FullName;
                        Session["login"] = 1;
                        return RedirectToAction("CustomerHome", new { customerID = cst.CustomerID });
                    }
                    else
                    {
                        ViewBag.Message = "Error Logging!!!";
                        return View();
                    }
                }
                catch
                {
                    ViewBag.Message = "Error Logging!!!";
                    return View();
                }
            }
        }

        public ActionResult Logout()
        {
            Session.Clear();
            return RedirectToAction("Login");
        }
    }
}