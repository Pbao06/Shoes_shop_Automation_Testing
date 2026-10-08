namespace MyApp 
{
     class VuKhi
    {
        public int dan{get;set;}
        public string loaisung{get;set;} = string.Empty;
        public VuKhi(){}
        public VuKhi(int amount ,string type)
        {
            dan=amount;
            loaisung=type;
            Console.WriteLine($" Khoi tao gia tri  cho sung la {dan} - {loaisung} ");
        }
    }
}