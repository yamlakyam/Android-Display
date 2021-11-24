package com.cnet.VisualAnalysis.Utils;

public class Constants {
    public static final String SummaryTableURL = "http://192.168.1.248:8001/api/ChartData/GetSalesDataForAllOrganizations";
    public static final String DistributorTableURL = "http://192.168.1.248:8001/api/ChartData/GetSalesDataForSingleOrganization";
    public static final String VsmCardURL = "http://192.168.1.248:8001/api/ChartData/GetSalesDataToDisplayVsmCards";
    public static final String VsmTransactionURL = "http://192.168.1.248:8001/api/ChartData/GetSalesDataToDisplayOnVsmTable";
    public static final String DashboardURL = "http://192.168.1.248:8002/api/DashBoardData/GetDashBoardData";
    //    public static final String allDataWithConfigurationURL = "http://192.168.1.248:8001/api/DashBoardData/GetDataWithConfiguration?imei=cc70a81e8233444a";
//    public static final String allDataWithConfigurationURL = "http://192.168.1.248:8001/api/DashBoardData/GetDataWithConfiguration";
    public static final String allDataWithConfigurationURL = "http://192.168.1.167:8084/api/DashBoardData/GetDataWithConfiguration";

    public static final String IP_ADDRESS = "192.168.1.248";
    public static final int PORT_1 = 8001;
    public static final int PORT_2 = 8002;

    public static final String DONUT_TYPE = "donut";
    public static final String PIE_TYPE = "Pie";
    public static final String LINE_TYPE = "Line";
    public static final String BAR_TYPE = "Bar";

    public static final int SUMMARY_OF_ARTICLE_INDEX = 3;
    public static final int SUMMARY_OF_parent_ARTICLE_INDEX = 4;
    public static final int SUMMARY_OF_CHILD_ARTICLE_INDEX = 5;
    public static final int SUMMARY_OF_LAST_X_MONTHS_INDEX = 6;
    public static final int SUMMARY_OF_LAST_X_DAYS_INDEX = 7;
    public static final int SUMMARY_OF_BRANCH_INDEX = 8;
    public static final int ALL_USER_REPORT_INDEX = 9;
    public static final int EACH_USER_REPORT_INDEX = 10;
    public static final int ALL_PEAK_HOUR_INDEX = 11;
    public static final int EACH_PEAK_HOUR_INDEX = 12;
    public static final int MAP_INDEX = 1;

    public static final String dateCriteriaFormat = "EEE, MMM dd, yyyy";

}
