//------------------------------------------------------------------------------
// <auto-generated>
//    This code was generated from a template.
//
//    Manual changes to this file may cause unexpected behavior in your application.
//    Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace E_Petrol_Website.Models
{
    using System;
    using System.Collections.Generic;
    
    public partial class Withdraw
    {
        public int WithdrawID { get; set; }
        public int CustomerID { get; set; }
        public int Quantity { get; set; }
        public System.DateTime Date { get; set; }
        public string IsFree { get; set; }
    
        public virtual Customer Customer { get; set; }
    }
}
