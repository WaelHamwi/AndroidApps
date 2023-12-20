using System;
using E_Petrol_Website.Models;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace E_Petrol_Website.Controllers
{
    public class GettingCompanyName
    {
        EPetrolDBEntities db = new EPetrolDBEntities();
        public static String getName()
        {
            var db = new EPetrolDBEntities();

            return db.CompanyNames.Find(1).Name;
        }

        
    }
}