using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace E_Petrol_Website.Models
{
    public class ReportItem
    {
        public String customerName
        {
            set; get;
        }

        public int vehicleNumber
        {
            set;
            get;
        }

        public int freeWithdrown
        {
            set;
            get;
        }

        public int balanceWithdrown
        {
            set;
            get;
        }
    }
}