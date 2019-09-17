package org.apache.ofbiz.humanres;

//import com.googlecode.mp4parser.h264.Debug;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.text.DateFormatSymbols;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import net.bytebuddy.dynamic.DynamicType.Builder.AbstractBase.Delegator;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilFormatOut;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.transaction.TransactionUtil;
import org.apache.ofbiz.humanres.moving.MovingEmployee;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.humanres.employee.Employee;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.humanres.incometaxservices.IncomeTaxServices;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.ofbiz.humanres.employee.EmployeePosition;
//import static org.mockito.Mockito.*;

import org.apache.ofbiz.humanres.payrollReport.ObjRecordSalarySlipAll;

/**
 * @author Rbab3a
 */
public class PayrollServices {

    public final static String module = PayrollServices.class.getName();
    public static final String securityProperties = "security.properties";
    public static final String resourceError = "HumanResErrorUiLabels";
    private static final String keyValue = UtilProperties.getPropertyValue(securityProperties, "login.secret_key_string");
    private static final int TRANS_ITEM_SEQUENCE_ID_DIGITS = 5; // this is the number of digits used for transItemSeqId: 00001, 00002...
    public static Vector<ObjRecordSalarySlipAll> listSalarySlipAll = new Vector<ObjRecordSalarySlipAll>();
    public static String totalCompanyGlobal = "", totalEmployeeGlobal = "";

    public static String getUserLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GenericValue userLogin = null;
        String id = "";
        try {
            userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

            id = (String) userLogin.getString("userLoginId");
//            response.getOutputStream().println(id);
//            response.getOutputStream().flush();
        } catch (Exception ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            response.getOutputStream().close();
        }
        return id;
    }

    public static String deleteFlag(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GenericValue userLogin = null;
        String id = "";
        try {
            userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
            id = (String) userLogin.getString("partyId");
            response.getOutputStream().println(id);
            response.getOutputStream().flush();
        } catch (Exception ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            response.getOutputStream().close();
        }
        return "done";
    }

    //    kouz
    public static String getMajorIdDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, GenericEntityException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("AllowenceType", true);
            for (GenericValue genericValue : gv1) {

                str += "<option value='" + genericValue.get("allowenceTypeId") + "'>" + genericValue.get("description") + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            out.close();
        }
        return "";

    }

    public static boolean getAddtionsAndDeductionsAllownces(HttpServletRequest request, HttpServletResponse response, String allowenceId) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        List<GenericValue> gv1 = null;
        criteria.put("allowenceId", allowenceId);
        boolean flag = false;
        System.out.println("allo "+allowenceId);
        try {
            gv1 = delegator.findList("Allowences", EntityCondition.makeCondition(criteria), null, null, null, true);
            System.out.println("gvl "+gv1);
            if (gv1.get(0).get("factorId").toString().equals("SAL_INCREMENT")) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;
    }

    public static String getEmpAllowncesData(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();

        String EmpAllownces_flag = request.getParameter("EmpAllownces_flag");
        String partyId = request.getParameter("partyId");
        String typeId = request.getParameter("typeId");
        criteria.put("partyId", partyId);
        criteria.put("typeId", typeId);
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();

        String endDate = "";
        LocalDate sysDate = LocalDate.now();
        LocalDate currentLocalDate = null;

        try {
            if (EmpAllownces_flag.equals("Add")) {
                List<GenericValue> list = delegator.findList("CompanyEmployeeAllowences", EntityCondition.makeCondition(criteria), null, null, null, true);
                for (int i = 0; i < list.size(); i++) {
                    if (getAddtionsAndDeductionsAllownces(request, response, list.get(i).get("allowenceId").toString()) == true) {
                        if (list.get(i).get("endDate") != null) {
                            endDate = list.get(i).get("endDate").toString();
                            String line[] = endDate.split("-");
                            currentLocalDate = LocalDate.of(Integer.valueOf(line[0]), Integer.valueOf(line[1]), Integer.valueOf(line[2]));
                        } else {
                            endDate = "";
                        }
//                        if (list.get(i).get("endDate") == null || sysDate.isBefore(currentLocalDate) || sysDate.equals(currentLocalDate)) {

                        jsonRes = new JSONObject();
                        jsonRes.put("Id", list.get(i).get("Id"));
                        jsonRes.put("partyId", list.get(i).get("partyId"));
                        jsonRes.put("allowenceId", getAllowenceDescription(request, list.get(i).get("allowenceId").toString()));
                        jsonRes.put("allowenceId_ID", list.get(i).get("allowenceId"));
                        jsonRes.put("TransDate", list.get(i).get("TransDate").toString());
                        jsonRes.put("calculationId", getCalculationTypeDescription(request, response, list.get(i).get("calculationId").toString()));
                        jsonRes.put("amount", list.get(i).get("amount"));
                        jsonRes.put("percentageAmount", list.get(i).get("percentageAmount"));
                        jsonRes.put("calculationAmount", list.get(i).get("calculationAmount"));
                        if (list.get(i).get("startDate") == null) {
                            jsonRes.put("startDate", null);
                        } else {
                            jsonRes.put("startDate", list.get(i).get("startDate").toString());
                        }

                        if (list.get(i).get("endDate") == null) {
                            jsonRes.put("endDate", null);
                        } else {
                            jsonRes.put("endDate", list.get(i).get("endDate").toString());
                        }

                        jsonRes.put("stabilityId", getStabilityIdDescription(request, response, list.get(i).get("stabilityId").toString()));
                        jsonRes.put("userLoginId", list.get(i).get("userLoginId"));

                        jsonData.add(jsonRes);
//                        }
                    }
                }
            } else {
                List<GenericValue> list = delegator.findList("CompanyEmployeeAllowences", EntityCondition.makeCondition(criteria), null, null, null, true);
                for (int i = 0; i < list.size(); i++) {
                    if (getAddtionsAndDeductionsAllownces(request, response, list.get(i).get("allowenceId").toString()) == false) {
                        if (list.get(i).get("endDate") != null) {
                            endDate = list.get(i).get("endDate").toString();
                            String line[] = endDate.split("-");
                            currentLocalDate = LocalDate.of(Integer.valueOf(line[0]), Integer.valueOf(line[1]), Integer.valueOf(line[2]));
                        } else {
                            endDate = "";
                        }
//                        if (list.get(i).get("endDate") == null || sysDate.isBefore(currentLocalDate) || sysDate.equals(currentLocalDate)) {
                        jsonRes = new JSONObject();
                        jsonRes.put("Id", list.get(i).get("Id"));
                        jsonRes.put("partyId", list.get(i).get("partyId"));
                        jsonRes.put("allowenceId", getAllowenceDescription(request, list.get(i).get("allowenceId").toString()));
                        jsonRes.put("allowenceId_ID", list.get(i).get("allowenceId"));
                        jsonRes.put("TransDate", list.get(i).get("TransDate").toString());
                        jsonRes.put("calculationId", getCalculationTypeDescription(request, response, list.get(i).get("calculationId").toString()));
                        jsonRes.put("amount", list.get(i).get("amount"));
                        jsonRes.put("percentageAmount", list.get(i).get("percentageAmount"));
                        jsonRes.put("calculationAmount", list.get(i).get("calculationAmount"));
                        ;
                        if (list.get(i).get("startDate") == null) {
                            jsonRes.put("startDate", null);
                        } else {
                            jsonRes.put("startDate", list.get(i).get("startDate").toString());
                        }

                        if (list.get(i).get("endDate") == null) {
                            jsonRes.put("endDate", null);
                        } else {
                            jsonRes.put("endDate", list.get(i).get("endDate").toString());
                        }
                        jsonRes.put("stabilityId", getStabilityIdDescription(request, response, list.get(i).get("stabilityId").toString()));
                        jsonRes.put("userLoginId", list.get(i).get("userLoginId"));

                        jsonData.add(jsonRes);
//                        }
                    }
                }
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();

            out.println(jsonData.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";

    }
//get employee allowences data for social security screen

    public static String getEmpAllowncesAddtionData(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();

        String EmpAllownces_flag = request.getParameter("EmpAllownces_flag");
        String partyId = request.getParameter("partyId");
        String typeId = request.getParameter("typeId");
        criteria.put("partyId", partyId);
        criteria.put("typeId", typeId);
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();

        String endDate = "";
        String startDate = "";
        LocalDate sysDate = LocalDate.now();
        LocalDate endDateLocalDate = null;
        LocalDate startDateLocalDate = null;

        try {
            if (EmpAllownces_flag.equals("Add")) {
                List<GenericValue> list = delegator.findList("CompanyEmployeeAllowences", EntityCondition.makeCondition(criteria), null, null, null, true);
                for (int i = 0; i < list.size(); i++) {
                    if (getAddtionsAndDeductionsAllownces(request, response, list.get(i).get("allowenceId").toString()) == true) {
                        if (list.get(i).get("endDate") != null) {
                            endDate = list.get(i).get("endDate").toString();
                            String line[] = endDate.split("-");
                            endDateLocalDate = LocalDate.of(Integer.valueOf(line[0]), Integer.valueOf(line[1]), Integer.valueOf(line[2]));
                        } else {
                            endDate = "";
                        }

                        if (list.get(i).get("startDate") != null) {
                            startDate = list.get(i).get("startDate").toString();
                            String line[] = startDate.split("-");
                            startDateLocalDate = LocalDate.of(Integer.valueOf(line[0]), Integer.valueOf(line[1]), Integer.valueOf(line[2]));
                        } else {
                            startDate = "";
                        }

                        if (sysDate.isBefore(startDateLocalDate)) {

                        } else if (list.get(i).get("endDate") == null || sysDate.isBefore(endDateLocalDate) || sysDate.isEqual(endDateLocalDate)) {

                            jsonRes = new JSONObject();
                            jsonRes.put("Id", list.get(i).get("Id"));
                            jsonRes.put("partyId", list.get(i).get("partyId"));
                            jsonRes.put("allowenceId", getAllowenceDescription(request, list.get(i).get("allowenceId").toString()));
                            jsonRes.put("allowenceId_ID", list.get(i).get("allowenceId"));
                            jsonRes.put("TransDate", list.get(i).get("TransDate").toString());
                            jsonRes.put("calculationId", getCalculationTypeDescription(request, response, list.get(i).get("calculationId").toString()));
                            jsonRes.put("amount", list.get(i).get("amount"));
                            jsonRes.put("percentageAmount", list.get(i).get("percentageAmount"));
                            jsonRes.put("calculationAmount", list.get(i).get("calculationAmount"));
                            if (list.get(i).get("startDate") == null) {
                                jsonRes.put("startDate", null);
                            } else {
                                jsonRes.put("startDate", list.get(i).get("startDate").toString());
                            }

                            if (list.get(i).get("endDate") == null) {
                                jsonRes.put("endDate", null);
                            } else {
                                jsonRes.put("endDate", list.get(i).get("endDate").toString());
                            }

                            jsonRes.put("stabilityId", getStabilityIdDescription(request, response, list.get(i).get("stabilityId").toString()));
                            jsonRes.put("userLoginId", list.get(i).get("userLoginId"));

                            jsonData.add(jsonRes);
                        }
                    }
                }
                jsonRes = new JSONObject();
                jsonRes.put("Id", "0");
                jsonRes.put("partyId", partyId);
                jsonRes.put("allowenceId", "Basic Salary");
                jsonRes.put("allowenceId_ID", "Basfic Salary");
                jsonRes.put("TransDate", getBasicSalaryTransDate(request, partyId));
                jsonRes.put("calculationId", getCalculationTypeDescription(request, response, "AMOUNT"));
                jsonRes.put("amount", getBasicSalary(request, partyId));
                jsonRes.put("percentageAmount", "");
                jsonRes.put("calculationAmount", "");
                jsonRes.put("startDate", getBasicSalaryTransDate(request, partyId));
                jsonRes.put("endDate", "");
                jsonRes.put("stabilityId", getStabilityIdDescription(request, response, "PERMANENT"));
                jsonRes.put("userLoginId", "");

                jsonData.add(jsonRes);

                Map<String, Object> criteria2 = new HashMap<String, Object>();
                String gradeId = getGradeIdByPartyId(request, partyId);
                criteria2.put("gradeId", gradeId);
                List<GenericValue> AllowencesGradeList = delegator.findList("AllowenceGrade", EntityCondition.makeCondition(criteria2), null, null, null, true);
                System.out.println("AllowencesGradeList : " + AllowencesGradeList);
                if (!AllowencesGradeList.isEmpty()) {
                    for (GenericValue salaryRow : AllowencesGradeList) {
                        if (getAddtionsAndDeductionsAllownces(request, response, salaryRow.get("allowenceId").toString()) == true) {
                            jsonRes = new JSONObject();

                            if (salaryRow.get("endDate") != null) {
                                endDate = salaryRow.get("endDate").toString();
                                String line[] = endDate.split("-");
                                endDateLocalDate = LocalDate.of(Integer.valueOf(line[0]), Integer.valueOf(line[1]), Integer.valueOf(line[2]));
                            } else {
                                endDate = "";
                            }

                            if (salaryRow.get("startDate") != null) {
                                startDate = salaryRow.get("startDate").toString();
                                String line[] = startDate.split("-");
                                startDateLocalDate = LocalDate.of(Integer.valueOf(line[0]), Integer.valueOf(line[1]), Integer.valueOf(line[2]));
                            } else {
                                startDate = "";
                            }

                            if (sysDate.isBefore(startDateLocalDate)) {

                            } else if (salaryRow.get("endDate") == null || sysDate.isBefore(endDateLocalDate) || sysDate.isEqual(endDateLocalDate)) {

                                jsonRes.put("Id", "AG" + salaryRow.get("allowenceGradeId"));
                                jsonRes.put("allowenceId_ID", salaryRow.get("allowenceId"));
                                jsonRes.put("amount", (BigDecimal) salaryRow.get("amount"));
//                        jsonRes.put("startDate", (java.sql.Date) salaryRow.get("startDate"));
                                jsonRes.put("TransDate", salaryRow.get("startDate").toString());
//                        jsonRes.put("endDate", (java.sql.Date) salaryRow.get("endDate"));

                                if (salaryRow.get("startDate") == null) {
                                    jsonRes.put("startDate", null);
                                } else {
                                    jsonRes.put("startDate", salaryRow.get("startDate").toString());
                                }

                                if (salaryRow.get("endDate") == null) {
                                    jsonRes.put("endDate", null);
                                } else {
                                    jsonRes.put("endDate", salaryRow.get("endDate").toString());
                                }

                                jsonRes.put("stabilityId", getStabilityIdDescription(request, response, "PERMANENT"));
                                jsonRes.put("calculationId", getCalculationTypeDescription(request, response, "AMOUNT"));

                                jsonRes.put("allowenceId", getAllowenceDescription(request, (String) salaryRow.get("allowenceId")));
                                jsonRes.put("factorId", getAllowenceFactorId(request, (String) salaryRow.get("allowenceId")));
                                jsonData.add(jsonRes);
                            }
                        }
                    }
                }

            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();

            out.println(jsonData.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";

    }

    public static String getEmpAllowncesSocialSecurityData(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;

        Map<String, String> criteria = new HashMap<String, String>();
        String partyId = request.getParameter("partyId");
        String allowenceId = request.getParameter("allowenceId");
        String typeId = request.getParameter("typeId");
        criteria.put("partyId", partyId);
        criteria.put("allowenceId", allowenceId);
        criteria.put("typeId", typeId);

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        JSONObject jsonRes;

        JSONArray jsonData = new JSONArray();
        try {
            List<String> listOrder = new ArrayList<>();
            listOrder.add("Id");
            List<GenericValue> list = delegator.findList("CompanyEmployeeAllowences", EntityCondition.makeCondition(criteria), null, listOrder, null, true);
            for (int i = 0; i < list.size(); i++) {
                jsonRes = new JSONObject();
                jsonRes.put("Id", list.get(i).get("Id"));
                jsonRes.put("partyId", list.get(i).get("partyId"));
                jsonRes.put("allowenceId", getAllowenceDescription(request, list.get(i).get("allowenceId").toString()));
                jsonRes.put("TransDate", list.get(i).get("TransDate").toString());
                jsonRes.put("calculationId", getCalculationTypeDescription(request, response, list.get(i).get("calculationId").toString()));
                jsonRes.put("percentageAmount", list.get(i).get("percentageAmount"));
                jsonRes.put("calculationAmount", list.get(i).get("calculationAmount"));
                jsonRes.put("amount", list.get(i).get("amount"));
                if (list.get(i).get("startDate") == null) {
                    jsonRes.put("startDate", null);
                } else {
                    jsonRes.put("startDate", list.get(i).get("startDate").toString());
                }

                if (list.get(i).get("endDate") == null) {
                    jsonRes.put("endDate", null);
                } else {
                    jsonRes.put("endDate", list.get(i).get("endDate").toString());
                }

                jsonRes.put("stabilityId", getStabilityIdDescription(request, response, list.get(i).get("stabilityId").toString()));
                jsonRes.put("userLoginId", list.get(i).get("userLoginId"));

                jsonData.add(jsonRes);

            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
//            Collections.sort(jsonData);
            out.println(jsonData.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";

    }

    public static String getComAllowncesData(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;

        Map<String, String> criteria = new HashMap<String, String>();
        String partyId = request.getParameter("partyId");
        String typeId = request.getParameter("typeId");
        System.out.println("partyId" + partyId);
//        criteria.put("partyId", partyId);
        criteria.put("typeId", typeId);

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        try {
            List<GenericValue> list = delegator.findList("CompanyEmployeeAllowences", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : list) {
                jsonRes = new JSONObject();
                jsonRes.put("Id", row.get("Id"));
                jsonRes.put("partyId", row.get("partyId"));
                jsonRes.put("allowenceId", getAllowenceDescription(request, row.get("allowenceId").toString()));
                jsonRes.put("TransDate", row.get("TransDate").toString());
                jsonRes.put("calculationId", getCalculationTypeDescription(request, response, row.get("calculationId").toString()));
                jsonRes.put("percentageAmount", row.get("percentageAmount"));
                jsonRes.put("calculationAmount", row.get("calculationAmount"));
                jsonRes.put("amount", row.get("amount"));
                if (row.get("startDate") == null) {
                    jsonRes.put("startDate", " ");
                } else {
                    jsonRes.put("startDate", row.get("startDate").toString());
                }
                if (row.get("endDate") == null) {
                    jsonRes.put("endDate", " ");
                } else {
                    jsonRes.put("endDate", row.get("endDate").toString());
                }
                jsonRes.put("stabilityId", getStabilityIdDescription(request, response, row.get("stabilityId").toString()));

                jsonData.add(jsonRes);
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";

    }

    public static List getDeductionsAllownces(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        List<GenericValue> gv1 = null;
        criteria.put("factorId", "SAL_DEDUCT");

        try {
            gv1 = delegator.findList("Allowences", EntityCondition.makeCondition(criteria), null, null, null, true);
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gv1;
    }

    public static String getAllowenceDescription(HttpServletRequest request, String allowenceId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        if (allowenceId == null) {
            return "";
        } else {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("allowenceId", allowenceId);
//            alkouz smaha heek 3amdan w ma bdo y3'erhaa 
            String str = "";
            try {
                List<GenericValue> list = delegator.findList("Allowences", EntityCondition.makeCondition(criteria), null, null, null, true);
                if (list.size() != 0) {
                    str += list.get(0).get("description");
                }
            } catch (GenericEntityException ex) {
                Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
            }
            return str;
        }
    }

    //        Rbab3aa
    public static String getReferenceDescription(HttpServletRequest request, String refId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        if (refId == null) {
            return "";
        } else {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("refId", refId);
            String refDescription = "";
            try {
                GenericValue result = delegator.findOne("References", criteria, true);
                refDescription = (String) result.get("description");

            } catch (GenericEntityException ex) {
                Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
            }
            return refDescription;
        }
    }

    public static String getDegreeDescription(HttpServletRequest request, String degreeId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        if (degreeId == null) {
            return "";
        } else {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("degreeId", degreeId);
            String degreeDescription = "";
            try {
                GenericValue result = delegator.findOne("Degree", criteria, true);
                if (result != null) {
                    System.out.println("result " + result);
                    degreeDescription = (String) result.get("description");
                } else {
                    degreeDescription = "-";
                }
            } catch (GenericEntityException ex) {
                Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
            }
            return degreeDescription;
        }
    }

    public static String getGradeDescription(HttpServletRequest request, String gradeId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        if (gradeId == null) {
            return "";
        } else {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("gradeId", gradeId);
            String gradeDescription = "";
            try {
                GenericValue result = delegator.findOne("Grade", criteria, true);
                if (result != null) {
                    gradeDescription = (String) result.get("description");
                } else {
                    gradeDescription = gradeId;
                }
            } catch (GenericEntityException ex) {
                Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
            }
            return gradeDescription;
        }
    }

    public static String getAllowenceGradeDescription(HttpServletRequest request, String allowenceId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        if (allowenceId == null) {
            return "";
        } else {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("allowenceId", allowenceId);
            String allowenceGradeDescription = "";
            try {
                GenericValue result = delegator.findOne("Allowences", criteria, true);
                allowenceGradeDescription = (String) result.get("description");
            } catch (GenericEntityException ex) {
                Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
            }
            return allowenceGradeDescription;
        }
    }

    public static String getJobGroupDescription(HttpServletRequest request, String jobGroupId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        if (jobGroupId == null) {
            return "";
        } else {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("jobGroupId", jobGroupId);
            String jobGroupDescription = "";
            try {
                GenericValue result = delegator.findOne("JobGroup", criteria, true);
                if (result != null) {
                    jobGroupDescription = (String) result.get("description");
                } else {
                    jobGroupDescription = "-";
                }
            } catch (GenericEntityException ex) {
                Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
            }
            return jobGroupDescription;
        }
    }

    public static String getCalculationTypeDescription(HttpServletRequest request, HttpServletResponse response, String calculationId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("calculationId", calculationId);
        String str = "";
        try {
            List<GenericValue> gv1 = delegator.findList("CalculationType", EntityCondition.makeCondition(criteria), null, null, null, true);
            str += gv1.get(0).get("description");

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    public static String getStabilityIdDescription(HttpServletRequest request, HttpServletResponse response, String stabilityId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("stabilityId", stabilityId);
        String str = "";
        try {
            List<GenericValue> gv1 = delegator.findList("Stability", EntityCondition.makeCondition(criteria), null, null, null, true);
            str += gv1.get(0).get("description");

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    //    kouz
    public static String getPaymentIdDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("PaymentTypeBi", true);
//            PrintWriter pw = new PrintWriter(response.getOutputStream());
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("paymentTypeId") + "'>" + genericValue.get("description") + "</option>";

            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            response.getOutputStream().close();
        }
        return "";
    }

    //    kouz
    public static String getRelatedToIdDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("RelatedTo", true);
//            PrintWriter pw = new PrintWriter(response.getOutputStream());
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("relatedToId") + "'>" + genericValue.get("description") + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            response.getOutputStream().close();
        }
        return "";
    }

    public static String getallowencesIdAddtionsDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("relatedToId", "RELATED_EMP");
//        criteria.put("allowenceId", "0");
        criteria.put("factorId", "SAL_INCREMENT");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findList("Allowences", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("allowenceId") + "'>" + genericValue.get("description") + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getallowencesIdDeductionsDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("relatedToId", "RELATED_EMP");
//         criteria.put("allowenceMajorId", "");
        criteria.put("factorId", "SAL_DEDUCT");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findList("Allowences", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue genericValue : gv1) {
                if (genericValue.get("allowenceId").toString().equals(getGlobalPayrollSetting(request, "SOCIAL_SECURITY_ALLOWENCES_ID")) || genericValue.get("allowenceId").toString().equals(getGlobalPayrollSetting(request, "INCOME_TAX_ALLOWENCES_ID"))) {
                } else {
                    str += "<option value='" + genericValue.get("allowenceId") + "'>" + genericValue.get("description") + "</option>";

                }
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getAllowencesIdDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        String str = "";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("Allowences", true);
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("allowenceId") + "'>" + genericValue.get("description") + "</option>";
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    //    kouz
    public static String getFactorIdDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("AllowenceFactor", true);
//            PrintWriter pw = new PrintWriter(response.getOutputStream());
            for (GenericValue genericValue : gv1) {

                str += "<option value='" + genericValue.get("factorId") + "'>" + genericValue.get("description") + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            response.getOutputStream().close();
        }
        return "";
    }

    public static String getGradeIdDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("Grade", true);
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("gradeId") + "'>" + genericValue.get("description") + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    // getPartyIdDropDown Fotm Employees
    public static String getPartyIdDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("roleTypeId", "EMPLOYEE");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findList("PartyRole", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue genericValue : gv1) {

                Map<String, String> criteria2 = new HashMap<String, String>();
                String partyId = genericValue.get("partyId").toString();
                criteria2.put("partyId", partyId);

                List<GenericValue> gv2 = delegator.findList("Person", EntityCondition.makeCondition(criteria2), null, null, null, true);
                for (GenericValue genericValue2 : gv2) {
                    str += "<option value='" + genericValue.get("partyId") + "'>" + genericValue.get("partyId") + " - " + genericValue2.get("firstName") + " " + genericValue2.get("lastName") + "</option>";
                }
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getGroupIdDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("JobGroup", true);
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("jobGroupId") + "'>" + genericValue.get("jobGroupId") + " - " + genericValue.get("description") + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getJopstatusIdDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("JobStatus", true);
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("jobStatusId") + "'>" + genericValue.get("jobStatusId") + " - " + genericValue.get("statusName") + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
//Ahmad Rbab'ah --  

    public static String getAllowencesAdditionTaxDetails(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        String partyId = request.getParameter("partyId");
        double basicSalary = 0.0;
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("partyId", partyId);
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        JSONObject jsonRes;
        JSONObject jsonSalary = new JSONObject();
        JSONArray jsonData = new JSONArray();
        JSONObject mainJson = new JSONObject();
        try {
//                GenericValue result = delegator.findOne("Degree", criteria, true);
//                basicSalary =  result.get("description");

            jsonSalary.put("partyId", partyId);
            jsonSalary.put("description", "الراتب الأساسي");
            jsonSalary.put("amount", 500);
            jsonData.add(jsonSalary);
            List<GenericValue> allowenceAdditionsEmp = delegator.findList("CompanyEmployeeAllowences", EntityCondition.makeCondition(criteria), null, null, null, true);

            for (GenericValue allowObj : allowenceAdditionsEmp) {
                if (getAddtionsAndDeductionsAllownces(request, response, allowObj.get("allowenceId").toString()) == true) {
                    jsonRes = new JSONObject();
                    jsonRes.put("Id", allowObj.get("Id"));
                    jsonRes.put("partyId", allowObj.get("partyId"));
                    jsonRes.put("description", getAllowenceDescription(request, allowObj.get("allowenceId").toString()));
                    jsonRes.put("amount", allowObj.get("amount"));
                    jsonData.add(jsonRes);
                }
            }

            mainJson.put("basicSalary", jsonSalary);
            mainJson.put("allowences", jsonData);
            System.out.println(mainJson.toString());
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(mainJson.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";

    }

    public static String getGradeDropDown_Promotion(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String jobGroupId = request.getParameter("jobGroupId");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;

        try {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("jobGroupId", jobGroupId);
            List<GenericValue> list = delegator.findList("Grade", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue genericValue : list) {
                str += "<option value='" + genericValue.get("gradeId") + "' selected='selected'>" + genericValue.get("description") + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(HumanResEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static List getGradeDropDown(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {

        Map<String, String> criteria = new HashMap<String, String>();
        HashMap<String, String> grade = null;
        ArrayList gradList = new ArrayList();
        criteria.put("jobGroupId", "1");
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> gradeList = delegator.findList("Grade", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue gradeRow : gradeList) {
                grade = new HashMap<String, String>();
                grade.put("gradeId", (String) gradeRow.get("gradeId"));
                grade.put("description", (String) gradeRow.get("description"));
                gradList.add(grade);
            }
            return gradList;

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //    get dropDown Degree From Current degree To last Degree in grade
    public static String getDegreeDropDownBounse(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {
        JSONArray jsonDegree = new JSONArray();
        PrintWriter out = null;
        Map<String, String> criteria = new HashMap<String, String>();
        String gradeId = request.getParameter("gradeId");
        String degreeId = request.getParameter("degreeId");
        criteria.put("gradeId", gradeId);
        int compareBasicSalary = 0;

        JSONObject jsonRes;

        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");

            BigDecimal basicSalary = getBasicSalaryByDegreeId(request, degreeId);

            List<GenericValue> degreeList = delegator.findList("Degree", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue degreeRow : degreeList) {
                compareBasicSalary = basicSalary.compareTo((BigDecimal) degreeRow.get("basicSalary"));
                if (compareBasicSalary == -1) {
                    jsonRes = new JSONObject();
                    jsonRes.put("degreeId", degreeRow.get("degreeId"));
                    jsonRes.put("degreeDesc", getDegreeDescription(request, (String) degreeRow.get("degreeId")));
                    jsonDegree.add(jsonRes);
                }
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonDegree.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    //    this Method recive Map and Value from map and return  the Key for this value from map
    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    //    get next Degree
    public static void getNewDegreeBounse(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {
        JSONArray jsonDegree = new JSONArray();
        PrintWriter out = null;
        Map<String, String> criteria = new HashMap<String, String>();
        Map<String, BigDecimal> basicSalaryMap = new HashMap<String, BigDecimal>();
        String gradeId = request.getParameter("gradeId");
        String degreeId = request.getParameter("degreeId");
        criteria.put("gradeId", gradeId);
        int compareBasicSalary = 0;
        JSONObject jsonRes;
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            BigDecimal basicSalary = getBasicSalaryByDegreeId(request, degreeId);
            List<GenericValue> degreeList = delegator.findList("Degree", EntityCondition.makeCondition(criteria), null, null, null, true);

            for (GenericValue degreeRow : degreeList) {
                compareBasicSalary = basicSalary.compareTo((BigDecimal) degreeRow.get("basicSalary"));
                if (compareBasicSalary == -1) {
                    basicSalaryMap.put((String) degreeRow.get("degreeId"), (BigDecimal) degreeRow.get("basicSalary"));
                }
            }
            System.out.println(basicSalaryMap.entrySet());
            BigDecimal minValue = java.util.Collections.min(basicSalaryMap.values());
            String degree = (String) getKeyFromValue(basicSalaryMap, minValue);
            jsonRes = new JSONObject();
            jsonRes.put("degreeId", degree);
            jsonRes.put("degreeDesc", getDegreeDescription(request, degree));
            jsonRes.put("basicSalary", minValue);

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonRes.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //    get All Degree Based On GradeId
    public static void getDegreeDropDown(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {

        PrintWriter out = null;
        Map<String, String> criteria = new HashMap<String, String>();
        String gradeId = request.getParameter("gradeId");
        criteria.put("gradeId", gradeId);
        JSONObject jsonRes;
        JSONArray jsonDegree = new JSONArray();
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> degreeList = delegator.findList("Degree", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue degreeRow : degreeList) {
                jsonRes = new JSONObject();
                jsonRes.put("degreeId", degreeRow.get("degreeId"));
                jsonRes.put("degreeDesc", getDegreeDescription(request, (String) degreeRow.get("degreeId")));
                jsonDegree.add(jsonRes);
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonDegree.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String getGradeDataJPA(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {

        PrintWriter out = null;
        String jobGroupId = request.getParameter("jobGroupId");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("jobGroupId", jobGroupId);
        JSONObject jsonRes;
        JSONArray jsonGrade = new JSONArray();
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> gradeList = delegator.findList("Grade", EntityCondition.makeCondition(criteria), null, null, null, true);

            for (GenericValue gradeRow : gradeList) {

                jsonRes = new JSONObject();
                jsonRes.put("gradeID", gradeRow.get("gradeId"));
                jsonRes.put("gradeDesc", gradeRow.get("description"));
                jsonRes.put("jobGroupDesc", getJobGroupDescription(request, (String) gradeRow.get("jobGroupId")));
                jsonRes.put("refDesc", getReferenceDescription(request, (String) gradeRow.get("refId")));
                jsonRes.put("refId", gradeRow.get("refId"));
                jsonRes.put("userLoginId", gradeRow.get("userLoginId"));
                jsonRes.put("jobGroupId", gradeRow.get("jobGroupId"));
                jsonGrade.add(jsonRes);
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonGrade.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getDegreeDataJPA(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {
        PrintWriter out = null;
        Map<String, String> criteria = new HashMap<String, String>();
        String gradeId = request.getParameter("gradeId");
        criteria.put("gradeId", gradeId);
        JSONObject jsonRes;
        JSONArray jsonDegree = new JSONArray();
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> degreeList = delegator.findList("Degree", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue degreeRow : degreeList) {
                jsonRes = new JSONObject();
                jsonRes.put("degreeID", degreeRow.get("degreeId"));
                jsonRes.put("gradeID", degreeRow.get("gradeId"));
                jsonRes.put("refDesc", getReferenceDescription(request, (String) degreeRow.get("refId")));
                jsonRes.put("degreeDesc", getDegreeDescription(request, (String) degreeRow.get("degreeId")));
                jsonRes.put("gradeDesc", getGradeDescription(request, (String) degreeRow.get("gradeId")));
                jsonRes.put("refId", degreeRow.get("refId"));
                jsonRes.put("basicSalary", degreeRow.get("basicSalary"));
                jsonRes.put("remark", degreeRow.get("remarks"));
                jsonRes.put("userLoginId", degreeRow.get("userLoginId"));
                jsonDegree.add(jsonRes);
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonDegree.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getAllowenceGradeDataJPA(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {
        java.sql.Date endDate = null;
        PrintWriter out = null;
        Map<String, String> criteria = new HashMap<String, String>();
        String gradeId = request.getParameter("gradeId");
        criteria.put("gradeId", gradeId);
        JSONObject jsonRes;
        JSONArray jsonAllowGrade = new JSONArray();
        try {
            LocalDate sysDate = LocalDate.now();
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            LocalDate currentLocalDate = null;
            List<GenericValue> allowenceGradeList = delegator.findList("AllowenceGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue allowenceGradeRow : allowenceGradeList) {

                if (allowenceGradeRow.get("endDate") != null) {
                    endDate = (java.sql.Date) allowenceGradeRow.get("endDate");
                } else {
                    endDate = null;
                }

                if (endDate == null || sysDate.isBefore(endDate.toLocalDate()) || sysDate.isEqual(endDate.toLocalDate())) {
                    jsonRes = new JSONObject();

                    jsonRes.put("allowGradeId", allowenceGradeRow.get("allowenceGradeId"));
                    jsonRes.put("allowenceId", allowenceGradeRow.get("allowenceId"));
                    jsonRes.put("gradeID", allowenceGradeRow.get("gradeId"));
                    jsonRes.put("amount", allowenceGradeRow.get("amount"));

                    jsonRes.put("refDesc", getReferenceDescription(request, (String) allowenceGradeRow.get("refId")));
                    jsonRes.put("gradeDesc", getGradeDescription(request, (String) allowenceGradeRow.get("gradeId")));
                    jsonRes.put("allowDesc", getAllowenceGradeDescription(request, (String) allowenceGradeRow.get("allowenceId")));
                    jsonRes.put("refId", allowenceGradeRow.get("refId"));
                    jsonRes.put("startDate", allowenceGradeRow.get("startDate").toString());
                    jsonRes.put("endDate", endDate);
                    jsonRes.put("userLoginId", allowenceGradeRow.get("userLoginId").toString());
                    jsonAllowGrade.add(jsonRes);
                }
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonAllowGrade.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    //    -------------- end rbab3a code -------------
    public static String getRefrencesDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        String relatedTo = request.getParameter("relatedTo");
        criteria.put("relatedToId", relatedTo);
        String str = "<select> <option></option>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findList("References", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("refId") + "'>" + genericValue.get("description") + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getAllowencesGradeIdDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();

        criteria.put("relatedToId", "RELATED_GRADE");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findList("Allowences", EntityCondition.makeCondition(criteria), null, null, null, true);

//            List<GenericValue> gv1 = delegator.findAll("Allowences", true);
            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("allowenceId") + "'>" + genericValue.get("description") + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String setPartyId(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String partyId = request.getParameter("partyId");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("partyId", partyId);

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        try {
            List<GenericValue> partyIdList = delegator.findList("Person", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (partyIdList.size() != 0) {
                String fullName = partyIdList.get(0).get("firstName") + " " + partyIdList.get(0).get("middleName") + " " + partyIdList.get(0).get("lastName");

                try {
                    response.getOutputStream().println(fullName);
                    response.getOutputStream().flush();
                } catch (IOException ex) {
                    Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            response.getOutputStream().close();
        }
        return "";
    }

    public static String deleteAllowence(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        response.setContentType("String");
        String allowenceId = request.getParameter("allowenceId");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        try {

            List<EntityCondition> conditionList = new ArrayList<EntityCondition>();
            conditionList.add(EntityCondition.makeCondition("allowenceId", EntityOperator.EQUALS, allowenceId));

            if (delegator.removeByCondition("Allowences", EntityCondition.makeCondition(conditionList, EntityOperator.AND)) == 1) {
                response.getOutputStream().print("Success");
                response.getOutputStream().flush();
            } else {
                response.getOutputStream().print("ERR");
                response.getOutputStream().flush();
            }

        } catch (Exception ex) {

            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);

        } finally {

            response.getOutputStream().close();
        }

        return "Success";
    }

    public static Map<String, Object> updateCurrentFlaq(DispatchContext dctx, Map<String, ? extends Object> context) {
        Map<String, Object> results = new HashMap<String, Object>();
        Delegator delegator = dctx.getDelegator();
        Locale locale = (Locale) context.get("locale");

        String partyId = (String) context.get("partyId");
        String currentFlag = (String) context.get("currentFlag");
        String EmpJobId = (String) context.get("EmpJobId");

        int seq = Integer.parseInt(delegator.getNextSeqId("EmployeeJob")) - 1;

        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("partyId", partyId);

        try {
            List<GenericValue> list = delegator.findList("EmployeeJob", EntityCondition.makeCondition(criteria), null, null, null, true);
            GenericValue gvValue = null;
            for (int i = 0; i < list.size(); i++) {
                gvValue = (GenericValue) list.get(i).clone();
                int EmpJobId_int = Integer.parseInt((String) list.get(i).get("EmpJobId"));
                if (EmpJobId_int == seq) {
                    gvValue.set("currentFlag", "1");
                } else {
                    gvValue.set("currentFlag", "0");
                }

            }
            gvValue.store();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        return results;
    }

    public static String deleteJobClass(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        response.setContentType("String");
        String jobClassId = request.getParameter("jobClassId");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        try {

            List<EntityCondition> conditionList = new ArrayList<EntityCondition>();
            conditionList.add(EntityCondition.makeCondition("jobClassId", EntityOperator.EQUALS, jobClassId));

            if (delegator.removeByCondition("JobClass", EntityCondition.makeCondition(conditionList, EntityOperator.AND)) == 1) {
                response.getOutputStream().print("Success");
                response.getOutputStream().flush();
            } else {
                response.getOutputStream().print("ERR");
                response.getOutputStream().flush();
            }

        } catch (Exception ex) {

            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);

        } finally {

            response.getOutputStream().close();
        }

        return "Success";
    }

    //ajarmeh
    //ajarmeh
    public static String getCalculationIddDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("CalculationType", true);

            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("calculationId") + "'>" + genericValue.get("description") + "</option>";

            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    //ajarmeh

    public static String getStabilityIndexIdDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("Stability", true);

            for (GenericValue genericValue : gv1) {
                str += "<option value='" + genericValue.get("stabilityId") + "'>" + genericValue.get("description") + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            response.getOutputStream().close();
        }
        return "";
    }

    public static String getReferencesData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        try {
            List<GenericValue> list = delegator.findAll("References", true);
            for (int i = 0; i < list.size(); i++) {

                jsonRes = new JSONObject();
                jsonRes.put("refId", list.get(i).get("refId"));
                jsonRes.put("description", list.get(i).get("description"));
                jsonRes.put("refDate", list.get(i).get("refDate").toString());
                jsonRes.put("refNum", list.get(i).get("refNum"));
                jsonRes.put("refStartDate", list.get(i).get("refStartDate").toString());
                jsonRes.put("relatedToId", getRelatedToDescription(request, response, list.get(i).get("relatedToId").toString()));

                jsonData.add(jsonRes);
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";

    }

    public static String getRelatedToDescription(HttpServletRequest request, HttpServletResponse response, String relatedToId) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("relatedToId", relatedToId);
        String str = "";
        try {
            List<GenericValue> gv1 = delegator.findList("RelatedTo", EntityCondition.makeCondition(criteria), null, null, null, true);

            str += gv1.get(0).get("description");

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    public static String getAllowencesData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        try {
            List<GenericValue> list = delegator.findAll("Allowences", true);
            for (int i = 0; i < list.size(); i++) {

                String debitCreditFlagEmp = list.get(i).get("debitCreditFlagEmp") != null ? list.get(i).get("debitCreditFlagEmp").toString() : "";
                String debitCreditFlagComp = list.get(i).get("debitCreditFlagComp") != null ? list.get(i).get("debitCreditFlagComp").toString() : "";

                jsonRes = new JSONObject();
                jsonRes.put("allowenceId", list.get(i).get("allowenceId"));
                jsonRes.put("relatedToId", getRelatedToDescription(request, response, list.get(i).get("relatedToId").toString()));
                jsonRes.put("allowenceTypeId", getAllowenceTypeDescription(request, response, list.get(i).get("allowenceTypeId").toString()));
                jsonRes.put("description", list.get(i).get("description"));
                jsonRes.put("paymentTypeId", getPaymentTypeDescription(request, response, list.get(i).get("paymentTypeId").toString()));
                jsonRes.put("remark", list.get(i).get("remark"));
                jsonRes.put("userLoginId", list.get(i).get("userLoginId"));
                jsonRes.put("factorId", getAllowenceFactorDescription(request, response, list.get(i).get("factorId").toString()));

                String effectOnTax = list.get(i).get("effectOnTax") != null ? list.get(i).get("effectOnTax").toString() : "";
                if (effectOnTax.equals("Y")) {
                    jsonRes.put("effectOnTax", "Yes");
                } else if (effectOnTax.equals("N")) {
                    jsonRes.put("effectOnTax", "No");
                }

                jsonRes.put("glAccountIdEmp", list.get(i).get("glAccountIdEmp"));
                jsonRes.put("glAccountIdComp", list.get(i).get("glAccountIdComp"));
                if (debitCreditFlagEmp.equals("C")) {
                    jsonRes.put("debitCreditFlagEmp", "Credit");
                } else if (debitCreditFlagEmp.equals("D")) {
                    jsonRes.put("debitCreditFlagEmp", "Debit");
                }
                if (debitCreditFlagComp.equals("C")) {
                    jsonRes.put("debitCreditFlagComp", "Credit");
                } else if (debitCreditFlagComp.equals("D")) {
                    jsonRes.put("debitCreditFlagComp", "Debit");
                }

                jsonData.add(jsonRes);
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";

    }

    public static String getAllowenceTypeDescription(HttpServletRequest request, HttpServletResponse response, String allowenceTypeId) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("allowenceTypeId", allowenceTypeId);
        String str = "";
        try {
            List<GenericValue> gv1 = delegator.findList("AllowenceType", EntityCondition.makeCondition(criteria), null, null, null, true);
            str += gv1.get(0).get("description");

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    public static String getPaymentTypeDescription(HttpServletRequest request, HttpServletResponse response, String paymentTypeId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("paymentTypeId", paymentTypeId);
        String str = "";
        try {
            List<GenericValue> gv1 = delegator.findList("PaymentTypeBi", EntityCondition.makeCondition(criteria), null, null, null, true);
            str += gv1.get(0).get("description");
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    public static String getAllowenceFactorDescription(HttpServletRequest request, HttpServletResponse response, String factorId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("factorId", factorId);
        String str = "";
        try {
            List<GenericValue> gv1 = delegator.findList("AllowenceFactor", EntityCondition.makeCondition(criteria), null, null, null, true);
            str += gv1.get(0).get("description");
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    public static String setGlobalPayrollSettings(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String ALLOWENCES_ID = request.getParameter("AlloenceID");
        String EMP_CONTRIBUTION_RATE = request.getParameter("EMP_CONTRIBUTION_RATE");
        String COM_CONTRIBUTION_RATE = request.getParameter("COM_CONTRIBUTION_RATE");
        Map<String, String> criteriaEMP = new HashMap<String, String>();
        criteriaEMP.put("Key", "EMP_CONTRIBUTION_RATE");

        Map<String, String> criteriaCOM = new HashMap<String, String>();
        criteriaCOM.put("Key", "COM_CONTRIBUTION_RATE");

        Map<String, String> criteriaALLOW = new HashMap<String, String>();
        criteriaALLOW.put("Key", "SOCIAL_SECURITY_ALLOWENCES_ID");

        try {
            //            creaet EMP CONTRIBUTION RATE
            GenericValue resultEMP = delegator.findOne("GlobalPayrollSettings", criteriaEMP, true);
            System.out.println("resultEMP " + resultEMP);
            if (resultEMP == null) {
                GenericValue gvValue = delegator.makeValue("GlobalPayrollSettings");
                gvValue.set("Key", "EMP_CONTRIBUTION_RATE");
                gvValue.set("Value", EMP_CONTRIBUTION_RATE);
                gvValue.set("globalKey", "SOCIAL_SECURITY");
                gvValue.create();
            } else if (resultEMP != null) {
                GenericValue gvValue = (GenericValue) resultEMP.clone();
                gvValue.set("Value", EMP_CONTRIBUTION_RATE);
                gvValue.store();
            }
            //            creaet COM CONTRIBUTION RATE
            GenericValue resultCOM = delegator.findOne("GlobalPayrollSettings", criteriaCOM, true);
            if (resultCOM == null) {
                GenericValue gvValue = delegator.makeValue("GlobalPayrollSettings");
                gvValue.set("Key", "COM_CONTRIBUTION_RATE");
                gvValue.set("Value", COM_CONTRIBUTION_RATE);
                gvValue.set("globalKey", "SOCIAL_SECURITY");
                gvValue.create();
            } else if (resultCOM != null) {
                GenericValue gvValue = (GenericValue) resultCOM.clone();
                gvValue.set("Value", COM_CONTRIBUTION_RATE);
                gvValue.store();
            }
            //            creaet COM CONTRIBUTION RATE
            GenericValue resultALLOW = delegator.findOne("GlobalPayrollSettings", criteriaALLOW, true);
            if (resultALLOW == null) {
                GenericValue gvValue = delegator.makeValue("GlobalPayrollSettings");
                gvValue.set("Key", "SOCIAL_SECURITY_ALLOWENCES_ID");
                gvValue.set("Value", ALLOWENCES_ID);
                gvValue.set("globalKey", "SOCIAL_SECURITY");
                gvValue.create();
            } else if (resultALLOW != null) {
                GenericValue gvValue = (GenericValue) resultALLOW.clone();
                gvValue.set("Value", ALLOWENCES_ID);
                gvValue.store();
            }
//            
//            delegator.createOrStore(gvValue);
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
//              return ServiceUtil.returnError(ex.getMessage());
        }
        return "";

//                ServiceUtil.returnSuccess();
    }

    public static String setGlobalPayrollSetting(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String KEY = request.getParameter("Key");
        String VALUE = request.getParameter("Value");
        String GLOBALKEY = request.getParameter("globalKey");
        Map<String, String> criteriaEMP = new HashMap<String, String>();
        criteriaEMP.put("Key", KEY);

//         criteriaEMP.put("Value", VALUE);
//        criteriaEMP.put("globalKey", GLOBALKEY);
        try {

            GenericValue resultEMP = delegator.findOne("GlobalPayrollSettings", criteriaEMP, true);
            System.out.println("resultEMP " + resultEMP);
            if (resultEMP == null) {
                GenericValue gvValue = delegator.makeValue("GlobalPayrollSettings");
                gvValue.set("Key", KEY);
                gvValue.set("Value", VALUE);
                gvValue.set("globalKey", GLOBALKEY);
                gvValue.create();
            } else {
                GenericValue gvValue = (GenericValue) resultEMP.clone();
                gvValue.set("Value", VALUE);
                gvValue.store();
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
//              return ServiceUtil.returnError(ex.getMessage());
            return "error";
        }
        return "success";

//                ServiceUtil.returnSuccess();
    }

    public static String getGlobalPayrollSetting(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {

        PrintWriter out = null;
        String Key = request.getParameter("Key");
        String globalKey = request.getParameter("globalKey");
        Map<String, String> criteria = new HashMap<String, String>();

        JSONObject jsonRes;
        JSONArray jsonGrade = new JSONArray();
        try {
            if (Key.isEmpty()) {
                criteria.put("globalKey", globalKey);
            } else {
                criteria.put("Key", Key);
                criteria.put("globalKey", globalKey);
            }
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> list = delegator.findList("GlobalPayrollSettings", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : list) {
                jsonRes = new JSONObject();
                jsonRes.put("Key", row.get("Key"));
                jsonRes.put("Value", row.get("Value"));
                jsonGrade.add(jsonRes);
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonGrade.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getBasicSalaryGlID(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {

        PrintWriter out = null;
        String allowenceId = request.getParameter("allowenceId");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("allowenceId", allowenceId);
        JSONObject jsonRes;
        JSONArray jsonGrade = new JSONArray();
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> list = delegator.findList("Allowences", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : list) {
                jsonRes = new JSONObject();
                jsonRes.put("Value", row.get("glAccountIdComp"));
                jsonGrade.add(jsonRes);
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonGrade.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getIncomeTaxSettingsData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        try {
            List<GenericValue> list = delegator.findAll("IncomTax", true);
            for (GenericValue row : list) {

                jsonRes = new JSONObject();
                jsonRes.put("IncomTaxId", row.get("IncomTaxId"));
                jsonRes.put("singleAmount", row.get("singleAmount"));
                jsonRes.put("marriedAmount", row.get("marriedAmount"));
                if (row.get("startDate") == null) {
                    jsonRes.put("startDate", null);
                } else {
                    jsonRes.put("startDate", row.get("startDate").toString());
                }
                if (row.get("endDate") == null) {
                    jsonRes.put("endDate", null);
                } else {
                    jsonRes.put("endDate", row.get("endDate").toString());
                }
                jsonRes.put("active", row.get("active"));

                jsonData.add(jsonRes);
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String getIncomeTaxDetailsSettingsData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String IncomeTaxId = request.getParameter("IncomeTaxId");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("IncomTaxId", IncomeTaxId);

        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        try {
            List<GenericValue> list = delegator.findList("IncomTaxDetails", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : list) {

                jsonRes = new JSONObject();
                jsonRes.put("IncomTaxDetailsId", row.get("IncomTaxDetailsId"));
                jsonRes.put("IncomTaxId", row.get("IncomTaxId"));
                jsonRes.put("amount", row.get("amount"));
                jsonRes.put("percentageCalculation", row.get("percentageCalculation"));

                jsonData.add(jsonRes);
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String getMartialStatus(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {

        PrintWriter out = null;
        String partyId = request.getParameter("partyId");
        Map<String, String> criteria = new HashMap<String, String>();
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();

        criteria.put("partyId", partyId);
        String maritalStatus = null;
        String maritalStatus_Amount = null;
        String Income_tax_Id = null;

        boolean wifeIsWorking = false;
        try {
            jsonRes = new JSONObject();
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> list = delegator.findList("Person", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : list) {
                if (row.get("maritalStatus") != null) {
                    maritalStatus = row.get("maritalStatus").toString();
                }
            }
            System.out.println("martial status " + maritalStatus);

            Map<String, String> familyMemberCriteria = new HashMap<String, String>();
            familyMemberCriteria.put("relatedTo", partyId);
            List<GenericValue> familyMemberList = delegator.findList("Person", EntityCondition.makeCondition(familyMemberCriteria), null, null, null, true);
            for (GenericValue row : familyMemberList) {
                if (row.get("familyMemberType").toString().equals("WIFE") && row.get("wifeIsWorking").toString().equals("Y")) {
                    wifeIsWorking = true;
                }
            }

            System.out.println("wifeIsWorking " + wifeIsWorking);

            if (maritalStatus != null) {
                List<GenericValue> IncomTaxlist = delegator.findAll("IncomTax", true);
                for (GenericValue row : IncomTaxlist) {
                    if (row.get("endDate") == null) {
                        Income_tax_Id = row.get("IncomTaxId").toString();
                        if (maritalStatus.equals("S")) {
                            maritalStatus_Amount = row.get("singleAmount").toString();
                        } else if (maritalStatus.equals("M") && wifeIsWorking == true) {
                            maritalStatus_Amount = row.get("singleAmount").toString();
                            maritalStatus = "M_and_Wife";
                        } else {
                            maritalStatus_Amount = row.get("marriedAmount").toString();
                        }
                    }
                }
            }
            jsonRes.put("maritalStatus_Amount", maritalStatus_Amount);
            jsonRes.put("Income_tax_Id", Income_tax_Id);
            jsonRes.put("maritalStatus", maritalStatus);

            jsonData.add(jsonRes);
            System.out.println("maritalStatus_Amount " + maritalStatus_Amount);

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    // To Calculate Income Tax And return Deducted Amount

    public static String getIncomeTaxDetailsData_byIncomeTaxId(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        String IncomTaxId = request.getParameter("IncomTaxId");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("IncomTaxId", IncomTaxId);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        String pattern = "#,##0.0#";
        DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);

        BigDecimal Tax_Of_Monthly_Salary = (BigDecimal) decimalFormat.parse(request.getParameter("Tax_Of_Monthly_Salary"));

        try {
            List<String> listOrder = new ArrayList<>();
            listOrder.add("IncomTaxDetailsId");
            List<GenericValue> list = delegator.findList("IncomTaxDetails", EntityCondition.makeCondition(criteria), null, listOrder, null, true);

            BigDecimal tax_amount = new BigDecimal(0);
            BigDecimal amount = new BigDecimal(0);
            BigDecimal percentageCalculation = new BigDecimal(0);
            for (GenericValue row : list) {
                amount = (BigDecimal) row.get("amount");
                percentageCalculation = (BigDecimal) row.get("percentageCalculation");

                if ((Tax_Of_Monthly_Salary.compareTo(amount) > 0) && (amount.compareTo(BigDecimal.ZERO) == 0)) {
                    tax_amount = tax_amount.add((Tax_Of_Monthly_Salary.multiply(percentageCalculation)).divide(new BigDecimal(100)));
                    break;
                } else if (Tax_Of_Monthly_Salary.compareTo(amount) > 0) {
                    Tax_Of_Monthly_Salary = Tax_Of_Monthly_Salary.subtract(amount);
                    tax_amount = tax_amount.add((amount.multiply(percentageCalculation)).divide(new BigDecimal(100)));
                } else {
                    tax_amount = tax_amount.add((Tax_Of_Monthly_Salary.multiply(percentageCalculation)).divide(new BigDecimal(100)));
                    break;
                }

            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(tax_amount.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    //    Rbaba3aa
    public static Map<String, Object> createBonus_Validation(DispatchContext ctx, Map<String, Object> context) throws SQLException, IOException, IOException, ParseException, GenericServiceException {
        LocalDispatcher dispatcher = ctx.getDispatcher();

        Locale locale = (Locale) context.get("locale");
        String Msg = "";
        Delegator delegator = ctx.getDelegator();
        String partyId = (String) context.get("partyId");
        boolean beganTransaction = false;
        String strTransDate = (String) context.get("TransDate");

        String newDegreeId = (String) context.get("degreeId");
        java.sql.Date sqlTransDate = convertFromStringToDate(strTransDate);
        boolean isExist = isCalculatedPositionGradation(delegator, partyId, sqlTransDate);

        int month_FromTransDate = sqlTransDate.toLocalDate().getMonthValue();
        int year_FromTransDate = sqlTransDate.toLocalDate().getYear();
        java.sql.Date lastTransDate = getMax_TransDate_Employee_SalaryGrade(delegator, partyId);
//        String transType = getLastEmployeeTransType_EmplSalaryGrade(delegator, partyId, lastTransDate);
        boolean isTransTypeExsist = isTransTypeExsist_Validation(delegator, partyId, sqlTransDate, "BONUS");
        System.out.println("isTransTypeExsist " + isTransTypeExsist);
        int month_FromLastTransDate = lastTransDate.toLocalDate().getMonthValue();
        int year_FromLastTransDate = lastTransDate.toLocalDate().getYear();

        String currentDegreeId = getDegreeId_ByMaxTransDate(delegator, partyId, lastTransDate);
        Map<String, String> EmplSalaryGradeTable = new HashMap<String, String>();
        EmplSalaryGradeTable.put("partyId", partyId);
        try {
//            VALIDATION : check if this date calculated or not
            if (isExist) {
                try {
                    beganTransaction = TransactionUtil.begin();
                    // only rollback the transaction if we started one...
                    TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning :Can't delete, this record is calculated"));
                    Msg = "the date you selected is calculated";
                    return ServiceUtil.returnError(Msg);
                } catch (GenericEntityException e) {
                    Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                    Msg = "the date you selected is calculated";
                    return ServiceUtil.returnError(Msg);
                }
            }

            if (currentDegreeId.equals(newDegreeId)) {
                try {
                    beganTransaction = TransactionUtil.begin();
                    // only rollback the transaction if we started one...
                    TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning : You select the same degree "));
                    Msg = "Warrning : You select the same degree ";
                    Map<String, String> messageMap = UtilMisc.toMap("errMessage", Msg);
                    String errMsg = UtilProperties.getMessage("CommonUiLabels", "CommonDatabaseProblem", messageMap, locale);
                    return ServiceUtil.returnError(Msg);
                } catch (GenericEntityException e) {
                    Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                    Msg = "Warrning :  You select the same degree ";
                    return ServiceUtil.returnError(Msg);
                }
            }
//bonus in same day or less 
            if (!sqlTransDate.toLocalDate().isEqual(lastTransDate.toLocalDate()) || !sqlTransDate.toLocalDate().isBefore(lastTransDate.toLocalDate())) {
                if (isTransTypeExsist) {
                    if (month_FromLastTransDate == month_FromTransDate && year_FromTransDate == year_FromLastTransDate) {
                        try {
                            beganTransaction = TransactionUtil.begin();
                            // only rollback the transaction if we started one...
                            TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning : You can't increase the employee's salary more than once in the same month "));
                            Msg = "Warrning :  You can't increase the employee's salary more than once in the same month ";
                            Map<String, String> messageMap = UtilMisc.toMap("errMessage", Msg);
                            String errMsg = UtilProperties.getMessage("CommonUiLabels", "CommonDatabaseProblem", messageMap, locale);
                            return ServiceUtil.returnError(Msg);

                        } catch (GenericEntityException e) {
                            Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                            Msg = "Warrning :  You can't increase the employee's salary more than once in the same month ";
                            return ServiceUtil.returnError(Msg);
                        }
                    }
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
            Map<String, String> messageMap = UtilMisc.toMap("errMessage", ex.getMessage());
            Msg = UtilProperties.getMessage("CommonUiLabels", "CommonDatabaseProblem", messageMap, locale);
            return ServiceUtil.returnError(Msg);
        }
        Map<String, String> messageMap = UtilMisc.toMap("successMessage", "Success");
        Msg = "A bonus has been added successfully to the employee";
        return ServiceUtil.returnSuccess(Msg);
    }

    public static Map<String, Object> createPromotion_Validation(DispatchContext ctx, Map<String, Object> context) throws SQLException, IOException, IOException, ParseException, GenericServiceException {
        LocalDispatcher dispatcher = ctx.getDispatcher();

        Locale locale = (Locale) context.get("locale");
        String Msg = "";
        Delegator delegator = ctx.getDelegator();
        String partyId = (String) context.get("partyId");
        boolean beganTransaction = false;
        String strTransDate = (String) context.get("TransDate");
        java.sql.Date sqlTransDate = convertFromStringToDate(strTransDate);
        boolean isExist = isCalculatedPositionGradation(delegator, partyId, sqlTransDate);
        int month_FromTransDate = sqlTransDate.toLocalDate().getMonthValue();
        int year_FromTransDate = sqlTransDate.toLocalDate().getYear();
        java.sql.Date lastTransDate = getMax_TransDate_Employee_SalaryGrade(delegator, partyId);
        System.out.println("lastTransDate " + lastTransDate);
        int month_FromLastTransDate = lastTransDate.toLocalDate().getMonthValue();
        int year_FromLastTransDate = lastTransDate.toLocalDate().getYear();

        boolean isTransTypeExsist = isTransTypeExsist_Validation(delegator, partyId, sqlTransDate, "PROMOTION");
        Map<String, String> EmplSalaryGradeTable = new HashMap<String, String>();
        EmplSalaryGradeTable.put("partyId", partyId);
        try {
            if (isExist) {
                try {
                    beganTransaction = TransactionUtil.begin();
                    // only rollback the transaction if we started one...
                    TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning :Can't delete, this record is calculated"));
                    Msg = " the date you selected is calculated";
                    return ServiceUtil.returnError(Msg);
                } catch (GenericEntityException e) {
                    Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, e);
                    Msg = "the date you selected is calculated";
                    return ServiceUtil.returnError(Msg);
                }
            }
            if (!sqlTransDate.toLocalDate().isEqual(lastTransDate.toLocalDate()) || !sqlTransDate.toLocalDate().isBefore(lastTransDate.toLocalDate())) {
                if (isTransTypeExsist) {
                    if (month_FromLastTransDate == month_FromTransDate && year_FromTransDate == year_FromLastTransDate) {
                        try {
                            beganTransaction = TransactionUtil.begin();
                            // only rollback the transaction if we started one...
                            TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning : You can't increase the employee's salary more than once in the same month "));
                            Msg = "Warrning :  You can't increase the employee's salary more than once in the same month ";
                            Map<String, String> messageMap = UtilMisc.toMap("errMessage", Msg);
//                                    String errMsg = UtilProperties.getMessage("CommonUiLabels", "CommonDatabaseProblem", messageMap, locale);
                            return ServiceUtil.returnError(Msg);
                        } catch (GenericEntityException e) {
                            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, e);
                            Msg = "Warrning :  You can't increase the employee's salary more than once in the same month ";
                            return ServiceUtil.returnError(Msg);
                        }

                    }
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            Map<String, String> messageMap = UtilMisc.toMap("errMessage", ex.getMessage());
            Msg = UtilProperties.getMessage("CommonUiLabels", "CommonDatabaseProblem", messageMap, locale);
            return ServiceUtil.returnError(Msg);
        }
        Map<String, String> messageMap = UtilMisc.toMap("successMessage", "Success");
        Msg = "Employee has been promoted successfully";
        return ServiceUtil.returnSuccess(Msg);
    }

    public static String getEmplPositionTypeId(HttpServletRequest request, String empPositionId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String emplPositionTypeId = "-";
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("emplPositionId", empPositionId);
        try {
            List<GenericValue> result = delegator.findList("EmplPosition", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : result) {
                emplPositionTypeId = (String) row.get("emplPositionTypeId");
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return emplPositionTypeId;

    }
//     max transDate > currentDate  

    public static java.sql.Date getMax_TransDate_Employee_SalaryGrade(HttpServletRequest request, String partyId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        if (partyId == null) {
            return (java.sql.Date) null;
        }
        criteria.put("partyId", partyId);
        ArrayList transDate = new ArrayList();
        java.sql.Date LocalTransDateMax = null;
        try {
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : result) {
                java.sql.Date TransDate = (java.sql.Date) row.get("TransDate");
                transDate.add(TransDate);

            }
            if (!transDate.isEmpty()) {
                LocalTransDateMax = (java.sql.Date) Collections.max(transDate);
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return LocalTransDateMax;

    }

    public static java.sql.Date getMax_TransDate_Employee_SalaryGrade(Delegator delegator, String partyId) throws ParseException, IOException {

        Map<String, String> criteria = new HashMap<String, String>();
        if (partyId == null) {
            return (java.sql.Date) null;
        }
        criteria.put("partyId", partyId);
        ArrayList transDate = new ArrayList();
        java.sql.Date LocalTransDateMax = null;
        try {
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : result) {
                java.sql.Date TransDate = (java.sql.Date) row.get("TransDate");
                transDate.add(TransDate);

            }
            if (!transDate.isEmpty()) {
                LocalTransDateMax = (java.sql.Date) Collections.max(transDate);
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return LocalTransDateMax;

    }

    //for Bonus and Promotion Services based on CurrentDate
    public static java.sql.Date getLastActive_TransDate_Employee_SalaryGrade(HttpServletRequest request, String partyId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        if (partyId == null) {
            return (java.sql.Date) null;
        }
        criteria.put("partyId", partyId);
        ArrayList transDate = new ArrayList();
        LocalDate sysDate = LocalDate.now();
        java.sql.Date LocalTransDateMax = null;
        try {
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                for (GenericValue row : result) {
                    java.sql.Date TransDate = (java.sql.Date) row.get("TransDate");
                    if (TransDate.toLocalDate().isBefore(sysDate) || TransDate.toLocalDate().isEqual(sysDate)) {
                        transDate.add(TransDate);
                    }
                }
            }
            if (!transDate.isEmpty()) {
                LocalTransDateMax = (java.sql.Date) Collections.max(transDate);
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return LocalTransDateMax;

    }

    //    when transDate
    public static String getDegreeId_ByMaxTransDate(Delegator delegator, String partyId, java.sql.Date transDateMax) throws ParseException, IOException {
        Map<String, Object> EmplSalaryGradeTable = new HashMap<String, Object>();
        EmplSalaryGradeTable.put("partyId", partyId);
        EmplSalaryGradeTable.put("TransDate", transDateMax);
        String degreeId = "";
        try {
            List<GenericValue> EmplSalaryGradeList = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(EmplSalaryGradeTable), null, null, null, true);
            for (GenericValue row : EmplSalaryGradeList) {

                degreeId = (String) row.get("degreeId");

            }
            return degreeId;
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        return degreeId;
    }

    public static java.sql.Date getLastActive_TransDate_Employee_SalaryGrade(Delegator delegator, String partyId) throws ParseException, IOException {

        Map<String, String> criteria = new HashMap<String, String>();
        if (partyId == null) {
            return (java.sql.Date) null;
        }
        criteria.put("partyId", partyId);
        ArrayList transDate = new ArrayList();
        LocalDate sysDate = LocalDate.now();
        java.sql.Date LocalTransDateMax = null;
        try {
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                for (GenericValue row : result) {
                    java.sql.Date TransDate = (java.sql.Date) row.get("TransDate");
                    if (TransDate.toLocalDate().isBefore(sysDate) || TransDate.toLocalDate().isEqual(sysDate)) {
                        transDate.add(TransDate);
                    }
                }
            }
            if (!transDate.isEmpty()) {
                LocalTransDateMax = (java.sql.Date) Collections.max(transDate);
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return LocalTransDateMax;

    }

    //    for Termiation Service
    public static String getLastActive_TransDate_Employee_SalaryGrade(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        String partyId = request.getParameter("partyId");
        criteria.put("partyId", partyId);
        ArrayList transDate = new ArrayList();
        LocalDate sysDate = LocalDate.now();
        java.sql.Date LocalTransDateMax = null;
        try {
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : result) {
                java.sql.Date TransDate = (java.sql.Date) row.get("TransDate");
                if (TransDate.toLocalDate().isBefore(sysDate) || TransDate.toLocalDate().isEqual(sysDate)) {
                    transDate.add(TransDate);
                }
            }
            LocalTransDateMax = (java.sql.Date) Collections.max(transDate);
            PrintWriter out = null;
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(LocalTransDateMax.toString());
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    //
    public static String getEmployeePositionDetails(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, IOException, ParseException {
        String partyId = request.getParameter("partyId");
        PrintWriter out = null;
        java.sql.Date LocalTransDateMax = getLastActive_TransDate_Employee_SalaryGrade(request, partyId);
        java.sql.Date lastTransDate = getMax_TransDate_Employee_SalaryGrade(request, partyId);

        String emplPositionTypeId = "";
        Map<String, Object> EmplSalaryGradeTable = new HashMap<String, Object>();
        EmplSalaryGradeTable.put("partyId", partyId);
        EmplSalaryGradeTable.put("TransDate", LocalTransDateMax);
        JSONObject jsonRes = new JSONObject();
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        try {
            //    get emplPostionId field from EmplPositionFulfillment Table
            List<GenericValue> EmplSalaryGradeList = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(EmplSalaryGradeTable), null, null, null, true);
            if (getEmployeeStatus(request, partyId).equals("HIRED")) {
                if (!EmplSalaryGradeList.isEmpty()) {

                    for (GenericValue row : EmplSalaryGradeList) {
                        jsonRes.put("partyId", row.get("partyId"));
                        jsonRes.put("tableId", row.get("emplSalaryGrade"));
                        jsonRes.put("transType", row.get("TransType").toString());
                        jsonRes.put("postionId", row.get("emplPositionId"));
                        jsonRes.put("jobGroupId", row.get("jobGroupId"));
                        jsonRes.put("jobGroupDesc", getJobGroupDescription(request, (String) row.get("jobGroupId")));
                        jsonRes.put("employment", row.get("partyIdFrom"));
                        jsonRes.put("gradeId", row.get("gradeId"));
                        jsonRes.put("gradeDesc", getGradeDescription(request, (String) row.get("gradeId")));
                        jsonRes.put("degreeId", row.get("degreeId"));
                        jsonRes.put("degreeDesc", getDegreeDescription(request, (String) row.get("degreeId")));
                        jsonRes.put("basicSalary", getBasicSalaryByDegreeId(request, (String) row.get("degreeId")));
                        jsonRes.put("TransDate", row.get("TransDate").toString());
                        jsonRes.put("lastTransDate", lastTransDate.toString());
                        jsonRes.put("movingDate", MovingEmployee.getFromDateEmploymentForEmployee(request, (String) row.get("partyId")));
                        jsonRes.put("fullName", HumanResEvents.getPartyName(request, response, partyId));
                        emplPositionTypeId = getEmplPositionTypeId(request, (String) row.get("emplPositionId"));
                        jsonRes.put("postionTypeId", emplPositionTypeId);
                        jsonRes.put("postionTypeDesc", getEmplPositionTypeDescription(request, emplPositionTypeId));
                        jsonRes.put("depDesciption", MovingEmployee.getDeprtmentDescription(request, (String) row.get("partyIdFrom")));
                    }
                }
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonRes.toString());
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getFromDateEmplFulFilmentForEmployee(HttpServletRequest request, String partyId, String currentPositionId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        Map<String, Object> fulFilmentTable = new HashMap<String, Object>();
        String maxSeq = getMaxSeq_EmplPositionFullFillment(delegator, partyId, true);
        fulFilmentTable.put("emplPositionFulfillmentId", maxSeq);
        fulFilmentTable.put("partyId", partyId);
        String fromDate = "";

        try {
            List<GenericValue> fulFilmentList = delegator.findList("EmplPositionFulfillment", EntityCondition.makeCondition(fulFilmentTable), null, null, null, true);
            for (GenericValue row : fulFilmentList) {
                if (!fulFilmentList.isEmpty()) {
                    fromDate = (String) row.get("fromDate").toString();
                }
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fromDate;

    }

    public static String getPromotionDetails(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, IOException, ParseException {
        String partyId = request.getParameter("empId");
        PrintWriter out = null;
        String emplPositionTypeId = "";
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        java.sql.Date LocalTransDateMax = getLastActive_TransDate_Employee_SalaryGrade(request, partyId);
        java.sql.Date lastTransDate = getMax_TransDate_Employee_SalaryGrade(request, partyId);

        Map<String, Object> EmplSalaryGradeTable = new HashMap<String, Object>();
        EmplSalaryGradeTable.put("partyId", partyId);
        EmplSalaryGradeTable.put("TransDate", LocalTransDateMax);
        JSONObject jsonRes = new JSONObject();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        try {

            //    get emplPostionId field from EmplPositionFulfillment Table
            List<GenericValue> EmplSalaryGradeList = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(EmplSalaryGradeTable), null, null, null, true);
            if (getEmployeeStatus(request, partyId).equals("HIRED")) {
                if (!EmplSalaryGradeList.isEmpty()) {
                    for (GenericValue row : EmplSalaryGradeList) {

                        jsonRes.put("partyId", row.get("partyId"));
                        jsonRes.put("postionId", row.get("emplPositionId"));
                        jsonRes.put("jobGroupId", row.get("jobGroupId"));
                        jsonRes.put("jobGroupDesc", getJobGroupDescription(request, (String) row.get("jobGroupId")));
                        jsonRes.put("employment", row.get("partyIdFrom"));
                        jsonRes.put("gradeId", row.get("gradeId"));
                        jsonRes.put("gradeDesc", getGradeDescription(request, (String) row.get("gradeId")));
                        jsonRes.put("degreeId", row.get("degreeId"));
                        jsonRes.put("degreeDesc", getDegreeDescription(request, (String) row.get("degreeId")));
                        jsonRes.put("basicSalary", getBasicSalaryByDegreeId(request, (String) row.get("degreeId")));
                        jsonRes.put("TransDate", row.get("TransDate").toString());
                        jsonRes.put("promotionDate", getFromDateEmplFulFilmentForEmployee(request, partyId, emplPositionTypeId));
                        jsonRes.put("fullName", HumanResEvents.getPartyName(request, response, partyId));
                        jsonRes.put("lastTransDate", lastTransDate.toString());
                        emplPositionTypeId = getEmplPositionTypeId(request, (String) row.get("emplPositionId"));
                        jsonRes.put("postionTypeId", emplPositionTypeId);
                        jsonRes.put("postionTypeDesc", getEmplPositionTypeDescription(request, emplPositionTypeId));
                    }
                }
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonRes.toString());
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getEmplPositionTypeDescription(HttpServletRequest request, String emplPositionTypeId) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String EmplPositionTypeDescription = "-";
        Map<String, String> EmplPositionTypeTable = new HashMap<String, String>();
        EmplPositionTypeTable.put("emplPositionTypeId", emplPositionTypeId);
        try {
            GenericValue EmplPositionTypeList = delegator.findOne("EmplPositionType", EmplPositionTypeTable, true);
            if (EmplPositionTypeList != null) {
                EmplPositionTypeDescription = (String) EmplPositionTypeList.get("description");
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return EmplPositionTypeDescription;
    }

    public static String getPositionEmployee(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String partyId = request.getParameter("partyId");
        PrintWriter out = null;
        String emplPositionTypeId = "";
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        java.sql.Date LocalTransDateMax = getLastActive_TransDate_Employee_SalaryGrade(request, partyId);
        Map<String, Object> EmplSalaryGradeTable = new HashMap<String, Object>();
        EmplSalaryGradeTable.put("partyId", partyId);
        EmplSalaryGradeTable.put("TransDate", LocalTransDateMax);

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String position = "";
        try {

            //    get emplPostionId field from EmplPositionFulfillment Table
            List<GenericValue> EmplSalaryGradeList = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(EmplSalaryGradeTable), null, null, null, true);
            if (EmplSalaryGradeList.size() != 0) {
                for (GenericValue row : EmplSalaryGradeList) {
                    emplPositionTypeId = getEmplPositionTypeId(request, (String) row.get("emplPositionId"));
                    position = getEmplPositionTypeDescription(request, emplPositionTypeId);
                    break;
                }
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(position);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getNewPostion(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String gradeId = request.getParameter("gradeId");
        String jobGroupId = request.getParameter("jobGroupId");
        PrintWriter out = null;
        Map<String, String> emplPostionTable = new HashMap<String, String>();
        JSONArray jsonData = new JSONArray();

        emplPostionTable.put("statusId", "EMPL_POS_ACTIVE");
        emplPostionTable.put("gradeId", gradeId);
        emplPostionTable.put("jobGroupId", jobGroupId);
        JSONObject jsonRes = new JSONObject();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        try {
            List<GenericValue> emplPositionList = delegator.findList("EmplPosition", EntityCondition.makeCondition(emplPostionTable), null, null, null, true);
            if (emplPositionList != null) {
                for (GenericValue row : emplPositionList) {
                    jsonRes = new JSONObject();
                    jsonRes.put("emplPositionId", row.get("emplPositionId"));
                    jsonRes.put("emplPositionTypeId", row.get("emplPositionTypeId"));
                    jsonRes.put("EmplPositionTypeDescription", getEmplPositionTypeDescription(request, (String) row.get("emplPositionTypeId")));
                    jsonData.add(jsonRes);
                }

            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static BigDecimal getBasicSalaryByDegreeId(HttpServletRequest request, String degreeId) {
        BigDecimal basicSalary = new BigDecimal(0);
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        try {
            Map<String, String> DegreeTable = new HashMap<String, String>();
            DegreeTable.put("degreeId", degreeId);
            GenericValue degreeList = delegator.findOne("Degree", DegreeTable, true);
            basicSalary = (BigDecimal) degreeList.get("basicSalary");

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return basicSalary;
    }

    public static BigDecimal getAllowenceGradeAmount(HttpServletRequest request, String gradeId) {
        BigDecimal basicSalary = new BigDecimal(0);
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        try {
            Map<String, String> DegreeTable = new HashMap<String, String>();
            DegreeTable.put("degreeId", gradeId);
            GenericValue degreeList = delegator.findOne("AllowenceGrade", DegreeTable, true);
            basicSalary = (BigDecimal) degreeList.get("basicSalary");

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return basicSalary;
    }

    //Service From Request
    public static BigDecimal getBasicSalaryByDegreeId(Delegator delegatorPram, String degreeId) {
        BigDecimal basicSalary = new BigDecimal(0);
        Delegator delegator = delegatorPram;

        try {
            Map<String, String> DegreeTable = new HashMap<String, String>();
            DegreeTable.put("degreeId", degreeId);
            GenericValue degreeList = delegator.findOne("Degree", DegreeTable, true);
            basicSalary = (BigDecimal) degreeList.get("basicSalary");

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return basicSalary;
    }
//Service From Request 

    public static String fillBasicSalary(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        BigDecimal basicSalary = new BigDecimal(0);
        String degreeId = request.getParameter("degreeId");

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        try {
            if (degreeId != null) {
                Map<String, String> DegreeTable = new HashMap<String, String>();
                DegreeTable.put("degreeId", degreeId);
                GenericValue degreeList = delegator.findOne("Degree", DegreeTable, true);
                basicSalary = (BigDecimal) degreeList.get("basicSalary");
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(basicSalary);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }
//From Request

    public static String getBasicSalaryByPartyId(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        BigDecimal basicSalary = new BigDecimal(0);
        String partyId = request.getParameter("partyId");
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        java.sql.Date LocalTransDateMax = getLastActive_TransDate_Employee_SalaryGrade(request, partyId);
        Map<String, Object> EmplSalaryGradeTable = new HashMap<String, Object>();
        EmplSalaryGradeTable.put("partyId", partyId);
        EmplSalaryGradeTable.put("TransDate", LocalTransDateMax);
        JSONObject jsonRes = new JSONObject();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String degreeId = "";
        try {
            List<GenericValue> EmplSalaryGradeList = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(EmplSalaryGradeTable), null, null, null, true);
            for (GenericValue row : EmplSalaryGradeList) {
                degreeId = (String) row.get("degreeId");
                basicSalary = getBasicSalaryByDegreeId(request, degreeId);
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(basicSalary.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    //    Get BasicSalary by PartyId From Method Call
    public static BigDecimal getBasicSalary(HttpServletRequest request, String partyId) throws ParseException, IOException {

        BigDecimal basicSalary = new BigDecimal(0);

        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        java.sql.Date LocalTransDateMax = getLastActive_TransDate_Employee_SalaryGrade(request, partyId);
        Map<String, Object> EmplSalaryGradeTable = new HashMap<String, Object>();
        EmplSalaryGradeTable.put("partyId", partyId);
        EmplSalaryGradeTable.put("TransDate", LocalTransDateMax);
        JSONObject jsonRes = new JSONObject();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String degreeId = "";
        try {
            List<GenericValue> EmplSalaryGradeList = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(EmplSalaryGradeTable), null, null, null, true);
            for (GenericValue row : EmplSalaryGradeList) {

                degreeId = (String) row.get("degreeId");
                basicSalary = getBasicSalaryByDegreeId(request, degreeId);

            }
            return basicSalary;
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        return basicSalary;
    }

    public static String getBasicSalaryTransDate(HttpServletRequest request, String partyId) throws ParseException, IOException {
        LocalDate today = LocalDate.now();
        java.sql.Date LocalTransDateMax = getLastActive_TransDate_Employee_SalaryGrade(request, partyId);
        Map<String, Object> EmplSalaryGradeTable = new HashMap<String, Object>();
        EmplSalaryGradeTable.put("partyId", partyId);
        EmplSalaryGradeTable.put("TransDate", LocalTransDateMax);
        JSONObject jsonRes = new JSONObject();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String TransDate = "";
        try {
            List<GenericValue> EmplSalaryGradeList = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(EmplSalaryGradeTable), null, null, null, true);
            for (GenericValue row : EmplSalaryGradeList) {
                TransDate = (String) row.get("TransDate").toString();
            }
            return TransDate;
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return TransDate;
    }

    public static Map<String, Object> updateThruDateEmplPosition(DispatchContext ctx, Map<String, Object> context) throws SQLException, IOException, IOException, ParseException, GenericServiceException {
        LocalDispatcher dispatcher = ctx.getDispatcher();

        Locale locale = (Locale) context.get("locale");
        boolean beganTransaction = false;
        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        String partyId = (String) context.get("partyId");
//      thruDate = The Promotion Date for new promotion 
        String thruDate = (String) context.get("thruDateUpdate");

//        FromDate start date for curent Promotion Action
        String fromDate = (String) context.get("fromDate");
        if (thruDate != null) {
            java.sql.Date sqlThruDate = convertFromStringToDate(thruDate);
            java.sql.Date sqlFromDate = convertFromStringToDate(fromDate);
            LocalDate endDate = null;
            String currentPositionId = (String) context.get("currentPositionId");
            if (sqlFromDate.toLocalDate().isEqual(sqlThruDate.toLocalDate())) {
                endDate = sqlFromDate.toLocalDate();
            } else {
                endDate = sqlFromDate.toLocalDate().minusDays(1);
            }
            String maxFulfillement = getMaxSeq_EmplPositionFullFillment(delegator, partyId, true);
            System.out.println("currentPositionId " + currentPositionId);
            Map<String, Object> currentPositionIdTable = new HashMap<String, Object>();
//            currentPositionIdTable.put("emplPositionId", currentPositionId);
//            currentPositionIdTable.put("partyId", partyId);
//            currentPositionIdTable.put("fromDate", sqlThruDate);
            currentPositionIdTable.put("emplPositionFulfillmentId", maxFulfillement);
            try {
                List<GenericValue> list = delegator.findList("EmplPositionFulfillment", EntityCondition.makeCondition(currentPositionIdTable), null, null, null, true);
                for (int i = 0; i < list.size(); i++) {
                    GenericValue gvValue = (GenericValue) list.get(i).clone();
                    gvValue.set("thruDate", java.sql.Date.valueOf(endDate));
                    gvValue.store();
                    System.out.println("Done Updated");

                }

            } catch (GenericEntityException ex) {
                Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                beganTransaction = TransactionUtil.begin();
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning : ThruDate null "));
                Msg = "Warrning : ThruDate null ";
                return ServiceUtil.returnError(Msg);
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                Msg = "Warrning : ThruDate null ";
                return ServiceUtil.returnError(Msg);
            }
        }

        return ServiceUtil.returnSuccess(Msg);
    }

    public static String getPreviousPositionId_EmplPositionFullFillment(Delegator delegator, String emplPositionFulfillmentId) throws ParseException, IOException {
        String positionId = "";
        Map<String, Object> EmplPositionFulfillmentTable = new HashMap<String, Object>();
        EmplPositionFulfillmentTable.put("emplPositionFulfillmentId", emplPositionFulfillmentId);
        try {
            List<GenericValue> EmplPositionFulfillmentTableList = delegator.findList("EmplPositionFulfillment", EntityCondition.makeCondition(EmplPositionFulfillmentTable), null, null, null, true);
            for (GenericValue row : EmplPositionFulfillmentTableList) {
                if (!EmplPositionFulfillmentTableList.isEmpty()) {
                    positionId = (String) row.get("emplPositionId");
                }
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return positionId;
    }

    public static String getPreviousPositionStatus_EmplPositionFullFillment(Delegator delegator, String positionId) throws ParseException, IOException {
        String status = "";
        Map<String, Object> EmplPositionTable = new HashMap<String, Object>();
        EmplPositionTable.put("emplPositionId", positionId);
        try {
            List<GenericValue> EmplPositionTableList = delegator.findList("EmplPosition", EntityCondition.makeCondition(EmplPositionTable), null, null, null, true);
            for (GenericValue row : EmplPositionTableList) {
                if (!EmplPositionTableList.isEmpty()) {
                    status = (String) row.get("statusId");
                }
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    public static Map<String, Object> updateEmplPositionStatus_DeletePromotion(DispatchContext ctx, Map<String, Object> context) throws SQLException, IOException, IOException, ParseException, GenericServiceException {
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        boolean beganTransaction = false;
        String currentPositionId = (String) context.get("positionId");
        String partyId = (String) context.get("partyId");
        String strTransDate = (String) context.get("transDate");
        String emplPositionFulfillmentId = getMaxSeq_EmplPositionFullFillment(delegator, partyId, false);
        String previousPositionId = getPreviousPositionId_EmplPositionFullFillment(delegator, emplPositionFulfillmentId);
        Map<String, String> currentPositionIdTable = new HashMap<String, String>();
        currentPositionIdTable.put("emplPositionId", currentPositionId);

        Map<String, String> previousPositionIdTable = new HashMap<String, String>();

        previousPositionIdTable.put("emplPositionId", previousPositionId);
        java.sql.Date sqlTransDate = convertFromStringToDate(strTransDate);
        String transType = getLastEmployeeTransType_EmplSalaryGrade(delegator, partyId, sqlTransDate);
        System.out.println("transType " + transType);
        if (!transType.equals("PROMOTION")) {
            try {
                beganTransaction = TransactionUtil.begin();
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning : this is not last action "));
                Msg = "Warrning : this is not last action ";
                return ServiceUtil.returnError(Msg);
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                Msg = "Warrning : this is not last action ";
                return ServiceUtil.returnError(Msg);
            }

        }

        try {

            beganTransaction = TransactionUtil.begin();

            List<GenericValue> list = delegator.findList("EmplPosition", EntityCondition.makeCondition(currentPositionIdTable), null, null, null, true);
            for (int i = 0; i < list.size(); i++) {
                GenericValue gvValue = (GenericValue) list.get(i).clone();
                gvValue.set("statusId", "EMPL_POS_ACTIVE");
                gvValue.store();
            }

            String posStatus = getPreviousPositionStatus_EmplPositionFullFillment(delegator, previousPositionId);
            if (posStatus.equals("EMPL_POS_ACTIVE")) {
                List<GenericValue> nextPositionIdList = delegator.findList("EmplPosition", EntityCondition.makeCondition(previousPositionIdTable), null, null, null, true);
                for (int i = 0; i < nextPositionIdList.size(); i++) {
                    GenericValue gvValue = (GenericValue) nextPositionIdList.get(i).clone();
                    gvValue.set("statusId", "EMPL_POS_OCCUPIED");
                    gvValue.store();
                }
            } else if (!posStatus.equals("EMPL_POS_ACTIVE")) {

                try {
                    beganTransaction = TransactionUtil.begin();
                    // only rollback the transaction if we started one...
                    TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning : Position OCCUPIED"));
                    Msg = " Warrning : The previous Position is OCCUPIED for another employee please delete it or create new position with same"
                            + " details about previous position ";
                    return ServiceUtil.returnError(Msg);
                } catch (GenericEntityException e) {
                    Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                    Msg = "Warrning : The previous Position is OCCUPIED for another employee please delete it or create new position with same"
                            + " details about previous position ";
                    return ServiceUtil.returnError(Msg);
                }

            }
            Msg = "Done";
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            // only commit the transaction if we started one... this will throw an exception if it fails
            try {
                TransactionUtil.commit(beganTransaction);

            } catch (GenericEntityException e) {
                Msg = e.getMessage();
                Debug.logError(e, "Could not commit transaction for entity engine error occurred while saving abandoned cart information", module);
                return ServiceUtil.returnError(Msg);
            }
        }

        Map<String, String> messageMap = UtilMisc.toMap("successMessage", "Success");
        String successMsg = UtilProperties.getMessage(resourceError, "deleteSuccess", messageMap, locale);
        return ServiceUtil.returnSuccess(successMsg);
    }

    public static Map<String, Object> updateThruDateEmplPosition_DeletePromotion(DispatchContext ctx, Map<String, Object> context) throws SQLException, IOException, IOException, ParseException, GenericServiceException {
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        String partyId = (String) context.get("partyId");
        String maxSeq = getMaxSeq_EmplPositionFullFillment(delegator, partyId, false);
        System.out.println("maxSeqFulfillment from updateThurDate to Null  Delete  " + maxSeq);
        Map<String, Object> currentPositionIdTable = new HashMap<String, Object>();
        currentPositionIdTable.put("emplPositionFulfillmentId", maxSeq);

        currentPositionIdTable.put("partyId", partyId);
        try {
            List<GenericValue> list = delegator.findList("EmplPositionFulfillment", EntityCondition.makeCondition(currentPositionIdTable), null, null, null, true);
            for (int i = 0; i < list.size(); i++) {
                GenericValue gvValue = (GenericValue) list.get(i).clone();
                gvValue.set("thruDate", (java.sql.Date) null);
                gvValue.store();
                System.out.println("Done Updated");

            }
            Map<String, String> messageMap = UtilMisc.toMap("successMessage", "Success");
            Msg = "Deleted successfully";
            return ServiceUtil.returnSuccess(Msg);
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        Map<String, String> messageMap = UtilMisc.toMap("successMessage", "Success");
        String successMsg = UtilProperties.getMessage(resourceError, "deleteSuccess", messageMap, locale);
        return ServiceUtil.returnSuccess(successMsg);
    }

    public static String getMaxSeq_EmplPositionFullFillment(Delegator delegator, String partyId, boolean isNull) throws ParseException, IOException {
        ArrayList maxSeqList = new ArrayList();
        String maxSeq = "";
        String rowValue = "";

        HashMap mapValues = new HashMap();

        Map<String, Object> EmplSalaryGradeTable = new HashMap<String, Object>();
        EmplSalaryGradeTable.put("partyId", partyId);
        try {
            List<GenericValue> EmplSalaryGradeList = delegator.findList("EmplPositionFulfillment", EntityCondition.makeCondition(EmplSalaryGradeTable), null, null, null, true);
            for (GenericValue row : EmplSalaryGradeList) {
                if (isNull == false) {
                    if (row.get("thruDate") != null) {
                        rowValue = (String) row.get("emplPositionFulfillmentId");
                        mapValues.put(Integer.valueOf(rowValue), rowValue);
                    }
                } else if (isNull == true) {
                    if (row.get("thruDate") == null) {
                        maxSeq = (String) row.get("emplPositionFulfillmentId");
                        System.out.println("getMaxSeq_EmplPositionFullFillment  " + maxSeq);
                        return maxSeq;
                    }

                }
            }
            if (!mapValues.isEmpty()) {
                int maxKey = (int) Collections.max(mapValues.keySet());
                System.out.println("Max value from Key" + mapValues.get(maxKey));
                maxSeq = (String) mapValues.get(maxKey);
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return maxSeq;
    }

    public static Map<String, Object> updateEmplPostionStatus(DispatchContext ctx, Map<String, Object> context) throws SQLException, IOException, IOException, ParseException, GenericServiceException {
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        String partyId = (String) context.get("partyId");
        String currentPositionId = (String) context.get("currentPositionId");
        String nextPositionId = (String) context.get("emplPositionId");
        Map<String, String> currentPositionIdTable = new HashMap<String, String>();
        currentPositionIdTable.put("emplPositionId", currentPositionId);
        Map<String, String> nextPositionIdTable = new HashMap<String, String>();
        boolean beganTransaction = false;
        nextPositionIdTable.put("emplPositionId", nextPositionId);
        try {
            beganTransaction = TransactionUtil.begin();

            List<GenericValue> list = delegator.findList("EmplPosition", EntityCondition.makeCondition(currentPositionIdTable), null, null, null, true);
            for (int i = 0; i < list.size(); i++) {
                GenericValue gvValue = (GenericValue) list.get(i).clone();
                gvValue.set("statusId", "EMPL_POS_ACTIVE");
                gvValue.store();
            }

            List<GenericValue> nextPositionIdList = delegator.findList("EmplPosition", EntityCondition.makeCondition(nextPositionIdTable), null, null, null, true);
            for (int i = 0; i < nextPositionIdList.size(); i++) {
                GenericValue gvValue = (GenericValue) nextPositionIdList.get(i).clone();
                gvValue.set("statusId", "EMPL_POS_OCCUPIED");
                gvValue.store();
            }

        } catch (GenericEntityException ex) {
            try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
                Msg = ex.getMessage();
                return ServiceUtil.returnError(Msg);
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                Msg = e.getMessage();
                return ServiceUtil.returnError(Msg);
            }

        } finally {
            // only commit the transaction if we started one... this will throw an exception if it fails
            try {
                TransactionUtil.commit(beganTransaction);
                return ServiceUtil.returnSuccess("success");
            } catch (GenericEntityException e) {
                Msg = e.getMessage();
                Debug.logError(e, "Could not commit transaction for entity engine error occurred while saving abandoned cart information", module);
                return ServiceUtil.returnError(Msg);
            }
        }

    }

    public static Map<String, Object> updateEmplSalaryGradeTransActive(DispatchContext ctx, Map<String, Object> context) throws SQLException, IOException, IOException, ParseException, GenericServiceException {
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        String partyId = (String) context.get("partyId");
        System.out.println(partyId);
        boolean beganTransaction = false;
        String strTransDate = (String) context.get("TransDate");
        java.sql.Date sqlTransDate = convertFromStringToDate(strTransDate);
        boolean isExist = PayrollServices.isCalculatedPositionGradation(delegator, partyId, sqlTransDate);
        Map<String, String> EmplSalaryGradeTable = new HashMap<String, String>();
        EmplSalaryGradeTable.put("partyId", partyId);
        try {
            if (!isExist) {
                List<GenericValue> list = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(EmplSalaryGradeTable), null, null, null, true);
                for (int i = 0; i < list.size(); i++) {
                    GenericValue gvValue = (GenericValue) list.get(i).clone();
//                    gvValue.set("TransActive", "0");
//                    gvValue.store();
                }
            } else {
                try {
                    beganTransaction = TransactionUtil.begin();
                    // only rollback the transaction if we started one...
                    TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning :Can't delete, this record is calculated"));
                    Msg = "";
                    return ServiceUtil.returnError(Msg);
                } catch (GenericEntityException e) {
                    Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                    Msg = "";
                    return ServiceUtil.returnError(Msg);
                }
            }

        } catch (GenericEntityException ex) {
            try {
                beganTransaction = TransactionUtil.begin();
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException(ex.getMessage()));
                Msg = ex.getMessage();
                return ServiceUtil.returnError(Msg);
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                Msg = e.getMessage();
                return ServiceUtil.returnError(Msg);
            }
        }
        Map<String, String> messageMap = UtilMisc.toMap("successMessage", "Success");
        Msg = "Employee has been promoted successfully";
        return ServiceUtil.returnSuccess(Msg);
    }

    public static String getEmployeeStatus(HttpServletRequest request, String partyId) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String emplStatus = "";
        Map<String, String> PersonEmployeeHiringTable = new HashMap<String, String>();
        PersonEmployeeHiringTable.put("partyId", partyId);
        try {
            GenericValue EmplPositionTypeList = delegator.findOne("Person", PersonEmployeeHiringTable, true);
            if (EmplPositionTypeList != null) {
                emplStatus = (String) EmplPositionTypeList.get("EmplStatus");
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return emplStatus;
    }

    public static String getEmployeeStatus(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String emplStatus = "";
        PrintWriter out = null;
        String partyId = request.getParameter("partyId");
        Map<String, String> creiteria = new HashMap<String, String>();
        creiteria.put("partyId", partyId);
        try {
            GenericValue ListValue = delegator.findOne("Person", creiteria, true);
            if (ListValue != null) {
                emplStatus = (String) ListValue.get("EmplStatus");
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(emplStatus);
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return emplStatus;
    }

    public static String getEmployeeSalaryGrade(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {
        PrintWriter out = null;
        Map<String, String> criteria = new HashMap<String, String>();

        String partyId = request.getParameter("partyId");

        criteria.put("partyId", partyId);
        String emplPositionTypeId = "";
        JSONObject jsonRes;
        JSONArray jsonEmplSalaryGrade = new JSONArray();

        try {

            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> EmplSalaryGradeList = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue salaryRow : EmplSalaryGradeList) {
                jsonRes = new JSONObject();
                jsonRes.put("rowSeq", salaryRow.get("emplSalaryGrade"));
                jsonRes.put("partyId", salaryRow.get("partyId"));
                jsonRes.put("TransDate", salaryRow.get("TransDate").toString());
                if (salaryRow.get("endDate") != null) {
                    jsonRes.put("endDate", salaryRow.get("endDate").toString());
                } else {
                    jsonRes.put("endDate", "");
                }

                jsonRes.put("emplPositionId", salaryRow.get("emplPositionId"));
                jsonRes.put("degreeDesc", getDegreeDescription(request, (String) salaryRow.get("degreeId")));
                jsonRes.put("gradeDesc", getGradeDescription(request, (String) salaryRow.get("gradeId")));
                jsonRes.put("basicSalary", getBasicSalaryByDegreeId(request, (String) salaryRow.get("degreeId")));
                jsonRes.put("TransType", salaryRow.get("TransType"));
//                jsonRes.put("TransActive", salaryRow.get("TransActive"));
                jsonRes.put("department", salaryRow.get("partyIdFrom"));
                jsonRes.put("notes", salaryRow.get("notes"));
                emplPositionTypeId = getEmplPositionTypeId(request, (String) salaryRow.get("emplPositionId"));
                jsonRes.put("emplPositionDescription", getEmplPositionTypeDescription(request, emplPositionTypeId));
                jsonRes.put("fullName", HumanResEvents.getPartyName(request, response, partyId));

                jsonEmplSalaryGrade.add(jsonRes);
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonEmplSalaryGrade.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    //  al ajarmeh

    //    to get BasicSalary  for each employee 
    public static Map getDataFromEmployeeSalaryGrade_SalaryCalculation(HttpServletRequest request, String partyId, int month, int year) throws SQLException, ParseException, IOException {
        Map<String, Object> criteria = new HashMap<String, Object>();
        LocalDate today = LocalDate.now();
        java.sql.Date LocalTransDateMax = getLastActive_TransDate_Employee_SalaryCalculation(request, partyId, month, year);
        String maxSeq = getMaxSequence_PositionGradation(request, partyId, LocalTransDateMax);
        criteria.put("partyId", partyId);
        criteria.put("TransDate", LocalTransDateMax);
        criteria.put("emplSalaryGrade", maxSeq);

        Map<String, Object> results = new HashMap<String, Object>();
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> EmplSalaryGradeList = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!EmplSalaryGradeList.isEmpty()) {
                for (GenericValue salaryRow : EmplSalaryGradeList) {
                    results.put("organizationPartyId", salaryRow.get("partyIdFrom"));
                    results.put("degreeId", salaryRow.get("degreeId"));
                    results.put("gradeId", salaryRow.get("gradeId"));
                    results.put("basicSalary", (BigDecimal) getBasicSalaryByDegreeId(request, (String) salaryRow.get("degreeId")));
                    results.put("jobGroupId", salaryRow.get("jobGroupId"));
                    results.put("positionId", salaryRow.get("emplPositionId"));
                    results.put("transDate", (java.sql.Date) salaryRow.get("TransDate"));
                }
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

    public static Map getDataFromEmployeeSalaryGrade_SalaryCalculation(Delegator delegatorPram, String partyId, int month, int year) throws SQLException, ParseException, IOException {
        Map<String, Object> criteria = new HashMap<String, Object>();
        LocalDate today = LocalDate.now();
        java.sql.Date LocalTransDateMax = getLastActive_TransDate_Employee_SalaryCalculation(delegatorPram, partyId, month, year);
        String maxSeq = getMaxSequence_PositionGradation(delegatorPram, partyId, LocalTransDateMax);
        criteria.put("partyId", partyId);
        criteria.put("TransDate", LocalTransDateMax);
        criteria.put("emplSalaryGrade", maxSeq);

        Map<String, Object> results = new HashMap<String, Object>();
        try {
            Delegator delegator = delegatorPram;
            List<GenericValue> EmplSalaryGradeList = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!EmplSalaryGradeList.isEmpty()) {
                for (GenericValue salaryRow : EmplSalaryGradeList) {
                    results.put("organizationPartyId", salaryRow.get("partyIdFrom"));
                    results.put("degreeId", salaryRow.get("degreeId"));
                    results.put("gradeId", salaryRow.get("gradeId"));
                    results.put("basicSalary", (BigDecimal) getBasicSalaryByDegreeId(delegatorPram, (String) salaryRow.get("degreeId")));
                    results.put("jobGroupId", salaryRow.get("jobGroupId"));
                    results.put("positionId", salaryRow.get("emplPositionId"));
                    results.put("transDate", (java.sql.Date) salaryRow.get("TransDate"));
                }
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

    public static String getGradeIdByPartyId(HttpServletRequest request, String partyId) throws ParseException, IOException {

        BigDecimal basicSalary = new BigDecimal(0);

        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        java.sql.Date LocalTransDateMax = getMax_TransDate_Employee_SalaryGrade(request, partyId);
        Map<String, Object> EmplSalaryGradeTable = new HashMap<String, Object>();
        EmplSalaryGradeTable.put("partyId", partyId);
        EmplSalaryGradeTable.put("TransDate", LocalTransDateMax);
        JSONObject jsonRes = new JSONObject();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String gradeId = "";
        try {
            List<GenericValue> EmplSalaryGradeList = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(EmplSalaryGradeTable), null, null, null, true);
            for (GenericValue row : EmplSalaryGradeList) {

                gradeId = (String) row.get("gradeId");

            }
            return gradeId;
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        return gradeId;
    }

    public static ArrayList<Map> getAllowenceGradeData_SalaryCalculation(HttpServletRequest request, String gradeId) throws SQLException, ParseException, IOException {
        Map<String, Object> criteria = new HashMap<String, Object>();
        ArrayList<Map> allAllowence = new ArrayList();
        criteria.put("gradeId", gradeId);

        Map<String, Object> results = null;
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> AllowencesGradeList = delegator.findList("AllowenceGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!AllowencesGradeList.isEmpty()) {
                for (GenericValue salaryRow : AllowencesGradeList) {
                    results = new HashMap<String, Object>();
                    results.put("allowenceId", salaryRow.get("allowenceId"));
                    results.put("amount", (BigDecimal) salaryRow.get("amount"));
                    results.put("startDate", (java.sql.Date) salaryRow.get("startDate"));
                    results.put("endDate", (java.sql.Date) salaryRow.get("endDate"));
                    results.put("description", getAllowenceDescription(request, (String) salaryRow.get("allowenceId")));
                    results.put("factorId", getAllowenceFactorId(request, (String) salaryRow.get("allowenceId")));
                    allAllowence.add(results);
                }
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allAllowence;
    }

    public static String getAllowenceFactorId(HttpServletRequest request, String allowenceId) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        List<GenericValue> AllowencesList = null;
        criteria.put("allowenceId", allowenceId);
        String factorId = "";
        try {
            AllowencesList = delegator.findList("Allowences", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue item : AllowencesList) {
                factorId = item.get("factorId").toString();
                break;
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return factorId;
    }

    public static String getAlloweceIdForBasicSalary(HttpServletRequest request) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("Key", "BASIC_SALARY_ALLOWENCES_ID");
        criteria.put("globalKey", "BASIC_SALARY");
        String allowenceId = "";
        List<GenericValue> result = null;
        try {
            result = delegator.findList("GlobalPayrollSettings", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue item : result) {
                allowenceId = (String) item.get("Value");
                break;
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allowenceId;
    }

    //
    public static String getUserLoginId(HttpServletRequest request) throws SQLException, ParseException, IOException {
        GenericValue userLogin = null;
        String id = "";
        try {
            userLogin = (GenericValue) request.getSession().getAttribute("userLogin");

            id = (String) userLogin.getString("userLoginId");
        } catch (Exception ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);

        }
        return id;
    }

    public static String getJobGroupId(HttpServletRequest request, String partyId) {

        Map<String, String> criteria = new HashMap<String, String>();
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        criteria.put("partyId", partyId);
        String jobGroupId = "";

        try {
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {

                for (GenericValue row : result) {

                    jobGroupId = (String) row.get("jobGroupId");
                    break;
                }
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jobGroupId;
    }

    //    for Salary Calculation
    public static String getDepartmentId(HttpServletRequest request, String partyId, int month, int year) throws ParseException, IOException {

        Map<String, Object> criteria = new HashMap<String, Object>();
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        criteria.put("partyId", partyId);
        String deptId = "";
        try {
            java.sql.Date LocalTransDateMax = getLastActive_TransDate_Employee_SalaryGrade(request, partyId);
            String maxSeq = getMaxSequence_PositionGradation(request, partyId, LocalTransDateMax);
            criteria.put("partyId", partyId);
            criteria.put("TransDate", LocalTransDateMax);
            criteria.put("emplSalaryGrade", maxSeq);
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                for (GenericValue row : result) {
                    deptId = (String) row.get("partyIdFrom");
                    break;
                }
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return deptId;
    }

    //        with current date  for BasicSalary Serives
    public static java.sql.Date getLastActive_TransDate_Employee_SalaryCalculation(HttpServletRequest request, String partyId, int calculationMonth, int calculationYear) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("partyId", partyId);

        LocalDate calculationStartDate = LocalDate.of(calculationYear, calculationMonth, 1);
        int lengthOfMonth = calculationStartDate.lengthOfMonth();
        LocalDate calculationEndDate = LocalDate.of(calculationYear, calculationMonth, lengthOfMonth);
        ArrayList transDateList = new ArrayList();
        java.sql.Date LocalTransDateMax = null;
        try {
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);

            if (!result.isEmpty()) {
                for (GenericValue row : result) {
                    java.sql.Date transDate = (java.sql.Date) row.get("TransDate");
                    LocalDate localTransDate = transDate.toLocalDate();
                    if ((localTransDate.isEqual(calculationStartDate) || localTransDate.isAfter(calculationStartDate) || localTransDate.isBefore(calculationStartDate))
                            && (localTransDate.isEqual(calculationEndDate) || localTransDate.isBefore(calculationEndDate))) {
                        transDateList.add((java.sql.Date) row.get("TransDate"));

                    }
                }
                if (!transDateList.isEmpty()) {
                    LocalTransDateMax = (java.sql.Date) Collections.max(transDateList);
                }
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return LocalTransDateMax;

    }

    public static String getGlAccountId(HttpServletRequest request, String allowenceId, String flag) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> criteria = new HashMap<String, String>();
        List<GenericValue> AllowencesList = null;
        criteria.put("allowenceId", allowenceId);
        String glAccountId = "";
        try {
            AllowencesList = delegator.findList("Allowences", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue item : AllowencesList) {
                if (flag.equals("Emp")) {
                    glAccountId = (String) item.get("glAccountIdEmp");
                } else if (flag.equals("Comp")) {
                    glAccountId = (String) item.get("glAccountIdComp");
                }
                break;
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
            return glAccountId;
        }
        return glAccountId;
    }

    public static int getNoOFDays_SalaryCalculation(java.sql.Date startDate, java.sql.Date endDate, LocalDate calculationStartDate, LocalDate calculationEndDate) {
//startDate = TransactionDate in From EmployeeSalaryGrade and AllowenceStartDate in allowences
//calculationEndDate end date for the month want to calculate 31/10/2016 
//calculationStartDate start date for the month want to calculate 1/10/2016 
        //28
        int lengthOfMonth = calculationStartDate.lengthOfMonth();
        int calculationYear = calculationStartDate.getYear();
        int calculationMonth = calculationStartDate.getMonthValue();
        int endMonth = 0;
        int endYear = 0;
        int endDay = 0;
        if(endDate ==null && endDate ==null &&calculationEndDate ==null){
            return (lengthOfMonth-calculationStartDate.getDayOfMonth());
        }
        if (endDate != null) {
            endMonth = endDate.toLocalDate().getMonthValue();
            endYear = endDate.toLocalDate().getYear();
            endDay = endDate.toLocalDate().getDayOfMonth();
        }
        if (endDate == null || calculationEndDate.isBefore(endDate.toLocalDate()) || calculationEndDate.isEqual(endDate.toLocalDate())) {
            int startYear = startDate.toLocalDate().getYear();
            int startMonth = startDate.toLocalDate().getMonthValue();
            int startDay = startDate.toLocalDate().getDayOfMonth();
            if (calculationYear == startYear) {
                if (startMonth == calculationMonth) {
                    if (startDay == 1) {
                        return lengthOfMonth;
                    } else {
                        return ((lengthOfMonth + 1) - startDay);
                    }
                } else {
                    return lengthOfMonth;
                }
            } else if (calculationYear > startYear) {
                return lengthOfMonth;
            }
        } else if (endYear == calculationYear && endMonth == calculationMonth) {
            return endDay;
        }

        return lengthOfMonth;
    }

    public static List<Map> getPreviousBasicSalary_SalaryCalculation(HttpServletRequest request, String partyId, int lengthOfMonthForCalculation, int numberOfDecimal, LocalDate startCalculationDate, LocalDate endCalculationDate) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String degreeId = "";
        String gradeId = "";
        double amount = 0d;
        BigDecimal roundAmount = new BigDecimal(0);
        java.sql.Date LocalTransDate = null;
        int lengthOfMonth = startCalculationDate.lengthOfMonth();
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("partyId", partyId);
        Map<String, Object> SalaryDetails = null;
        ArrayList<Map> allSalary = new ArrayList();
        String transType = "";
        String preiviousTransType = "";
        java.sql.Date endDate = null;

        int preivousTransDay = 0;
        try {
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                Collections.sort(result, Collections.reverseOrder());
                int lastDay = 0;
                int nomOfDays = 0;
                int startDayTransDate = 0;
                for (GenericValue row : result) {
                    LocalTransDate = (java.sql.Date) row.get("TransDate");
                    startDayTransDate = LocalTransDate.toLocalDate().getDayOfMonth();
                    if (LocalTransDate.toLocalDate().isAfter(startCalculationDate) && (LocalTransDate.toLocalDate().isEqual(endCalculationDate) || LocalTransDate.toLocalDate().isBefore(endCalculationDate))) {
                        transType = (String) row.get("TransType");
                        if (row.get("endDate") != null) {
                            endDate = (java.sql.Date) row.get("endDate");
                        }

                        degreeId = (String) row.get("degreeId");
                        gradeId = (String) row.get("gradeId");
                        //25

                        if (lastDay == 0) {
                            nomOfDays = (lengthOfMonth + 1) - startDayTransDate;
                            lastDay = startDayTransDate;
                        } else if (lastDay != 0) {
                            nomOfDays = lastDay - startDayTransDate;
                            lastDay = startDayTransDate;

                        }

                        if (transType.equals("TERMINATED")) {
                            preiviousTransType = "TERMINATED";
                        } else if (transType.equals("HOLD") && endDate == null) {
                            preivousTransDay = lastDay;
                            preiviousTransType = "HOLD";

                        } else {
                            if (preiviousTransType.equals("TERMINATED") && !transType.equals("HOLD")) {
                                nomOfDays++;
                                preiviousTransType = "";
                            } else if (transType.equals("HOLD") && endDate != null) {
                                preiviousTransType = "HOLD";
                                if (preivousTransDay != 0) {
                                    nomOfDays = (preivousTransDay - endDate.toLocalDate().getDayOfMonth()) - 1;
                                } else if (preivousTransDay == 0) {
                                    if (endDate.toLocalDate().isBefore(endCalculationDate)) {
                                        nomOfDays = lengthOfMonth - endDate.toLocalDate().getDayOfMonth();

                                    } else {
                                        nomOfDays = 0;
                                    }
                                }
                                preivousTransDay = lastDay;
                            }
//                            else if (!preiviousTransType.equals("HOLD") && transType.equals("HOLD")) {
//
//                                nomOfDays = (preivousTransDay - endDate.toLocalDate().getDayOfMonth()) - 1;
//                            }
//                            preivousTransDay=lastDay;

                            if (nomOfDays != 0) {
                                SalaryDetails = new HashMap<String, Object>();
                                SalaryDetails.put("numOfDays", new Long(nomOfDays));
                                amount = (getBasicSalaryByDegreeId(request, degreeId).doubleValue() / lengthOfMonthForCalculation) * nomOfDays;
                                roundAmount = new BigDecimal(amount);
                                roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);
                                SalaryDetails.put("degreeId", degreeId);
                                SalaryDetails.put("basicSalary", roundAmount.doubleValue());
                                SalaryDetails.put("gradeId", gradeId);
                                SalaryDetails.put("partyIdFrom", (String) row.get("partyIdFrom"));
                                SalaryDetails.put("positionId", (String) row.get("emplPositionId"));
                                SalaryDetails.put("jobGroupId", (String) row.get("jobGroupId"));
                                allSalary.add(SalaryDetails);
                            }
                        }
                    } else if (LocalTransDate.toLocalDate().isEqual(startCalculationDate)) {

                        transType = (String) row.get("TransType");
                        degreeId = (String) row.get("degreeId");
                        gradeId = (String) row.get("gradeId");

                        if (row.get("endDate") != null) {
                            endDate = (java.sql.Date) row.get("endDate");
                        }
                        if (transType.equals("HOLD") && endDate == null) {
                            break;
                        } else {
                            if (transType.equals("HOLD") && endDate != null) {
                                if(preivousTransDay==0){
                                    nomOfDays =lengthOfMonth-endDate.toLocalDate().getDayOfMonth();
                                } else {
                                nomOfDays = (preivousTransDay - endDate.toLocalDate().getDayOfMonth()) - 1;
                                preivousTransDay = lastDay; }
                            } else if (transType.equals("TERMINATED")) {
                                nomOfDays = 1;
                            } else if (preiviousTransType.equals("TERMINATED")) {
                                nomOfDays = lastDay;
                            } else {
                                nomOfDays = lastDay - 1;
                            }
                            if(nomOfDays ==0) {
                                break;
                            }
                            SalaryDetails = new HashMap<String, Object>();
                            SalaryDetails.put("numOfDays", new Long(nomOfDays));
                            amount = (getBasicSalaryByDegreeId(request, degreeId).doubleValue() / lengthOfMonthForCalculation) * nomOfDays;
                            roundAmount = new BigDecimal(amount);
                            roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);
                            System.out.println("amount " + roundAmount.doubleValue());
                            SalaryDetails.put("degreeId", degreeId);
                            SalaryDetails.put("gradeId", gradeId);
                            SalaryDetails.put("basicSalary", roundAmount.doubleValue());
                            SalaryDetails.put("partyIdFrom", (String) row.get("partyIdFrom"));
                            SalaryDetails.put("positionId", (String) row.get("emplPositionId"));
                            SalaryDetails.put("jobGroupId", (String) row.get("jobGroupId"));
                            allSalary.add(SalaryDetails);
                            break;

                        }
                    } else if (LocalTransDate.toLocalDate().isBefore(startCalculationDate)) {
                        if (preiviousTransType.equals("TERMINATED") && lastDay == lengthOfMonth) {
                            nomOfDays = 30;
                            preiviousTransType = "";
                        } else if (preiviousTransType.equals("TERMINATED") && lastDay != lengthOfMonth) {
                            nomOfDays = lastDay;
                            preiviousTransType = "";
                        } else {
                            nomOfDays = lastDay - 1;
                        }
                        degreeId = (String) row.get("degreeId");
                        gradeId = (String) row.get("gradeId");

                        SalaryDetails = new HashMap<String, Object>();
                        SalaryDetails.put("numOfDays", new Long(nomOfDays));
                        amount = (getBasicSalaryByDegreeId(request, degreeId).doubleValue() / lengthOfMonthForCalculation) * nomOfDays;
                        roundAmount = new BigDecimal(amount);
                        roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);
                        SalaryDetails.put("degreeId", degreeId);
                        SalaryDetails.put("gradeId", gradeId);
                        SalaryDetails.put("basicSalary", roundAmount.doubleValue());
                        SalaryDetails.put("partyIdFrom", (String) row.get("partyIdFrom"));
                        SalaryDetails.put("jobGroupId", (String) row.get("jobGroupId"));
                        SalaryDetails.put("positionId", (String) row.get("emplPositionId"));
                        allSalary.add(SalaryDetails);
                        break;
                    }
//                 
                }

                return allSalary;
            } else {
                return allSalary;
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allSalary;
    }

    public static List<List<Map>> getPreviousAllowenceGrade_SalaryCalculation(HttpServletRequest request, String partyId, int lengthOfMonthForCalculation, int numberOfDecimal, LocalDate startCalculationDate, LocalDate endCalculationDate) throws ParseException, SQLException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String degreeId = "";
        String gradeId = "";
        String factorId = "";
        BigDecimal roundAmount = new BigDecimal(0);

        double totalAmount = 0.0d;
        int dayOfStartAllow = 0;
        int dayOfLastTrans = 0;
        int numOfDaysGrade = 0;
        java.sql.Date LocalTransDate = null;
        int lengthOfMonth = startCalculationDate.lengthOfMonth();

        java.sql.Date allowenceStartDate = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("partyId", partyId);
        Map<String, Object> SalaryDetails = null;
        ArrayList<Map> allowenceGrade = null;
        List<List<Map>> allAllowencesGrade = new ArrayList<List<Map>>();
        List<Map> allowenceGradeList = new ArrayList();
        BigDecimal amount = new BigDecimal(0);
        String preivousGradeId = "";
        String transType = "";
        String preiviousTransType = "";
        int previousTransDay = 0;
        java.sql.Date endDate = null;

        try {
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                Collections.sort(result, Collections.reverseOrder());
                int lastDay = 0;
                int nomOfDays = 0;
                int startDayTransDate = 0;

                for (GenericValue row : result) {
                    LocalTransDate = (java.sql.Date) row.get("TransDate");
                    startDayTransDate = LocalTransDate.toLocalDate().getDayOfMonth();
                    if (LocalTransDate.toLocalDate().isAfter(startCalculationDate) && (LocalTransDate.toLocalDate().isEqual(endCalculationDate) || LocalTransDate.toLocalDate().isBefore(endCalculationDate))) {
                        if (row.get("endDate") != null) {
                            endDate = (java.sql.Date) row.get("endDate");
                        }
                        degreeId = (String) row.get("degreeId");
                        gradeId = (String) row.get("gradeId");
                        transType = (String) row.get("TransType");

                        preivousGradeId = gradeId;
                        allowenceGradeList = getAllowenceGradeData_SalaryCalculation(request, gradeId);
//                        System.out.println("allowenceGradeList " + gradeId + " " + allowenceGradeList);

                        System.out.println("LocalTransDate from service GetPreivousAllowence " + LocalTransDate);
                        if (lastDay == 0) {
                            nomOfDays = (lengthOfMonth + 1) - startDayTransDate;
                            lastDay = startDayTransDate;
                        } else if (lastDay != 0) {
                            nomOfDays = lastDay - startDayTransDate;
                            lastDay = startDayTransDate;
                        }

                        if (transType.equals("TERMINATED")) {
                            preiviousTransType = "TERMINATED";
                        } else if (transType.equals("HOLD") && endDate == null) {
                            previousTransDay = lastDay;

                        } else {
                            if (preiviousTransType.equals("TERMINATED") && !transType.equals("HOLD")) {
                                nomOfDays++;
                                preiviousTransType = "";
                            } else if (transType.equals("HOLD") && endDate != null) {
                                preiviousTransType = "HOLD";
                                if (previousTransDay != 0) {
                                    nomOfDays = (previousTransDay - endDate.toLocalDate().getDayOfMonth()) - 1;
                                } else if (previousTransDay == 0) {
                                    if (endDate.toLocalDate().isBefore(endCalculationDate)) {
                                        nomOfDays = lengthOfMonth - endDate.toLocalDate().getDayOfMonth();

                                    } else {
                                        nomOfDays = 0;
                                    }
                                }
                                previousTransDay = lastDay;
                            }
                            System.out.println("nomOfDays from service GetPreivousAllowence " + nomOfDays);
                            if (!allowenceGradeList.isEmpty()) {
                                allowenceGrade = new ArrayList();
                                for (int i = 0; i < allowenceGradeList.size(); i++) {
                                    allowenceStartDate = (java.sql.Date) allowenceGradeList.get(i).get("startDate");
                                    numOfDaysGrade = getNoOFDays_SalaryCalculation(allowenceStartDate, null, startCalculationDate, endCalculationDate);
                                    SalaryDetails = new HashMap<String, Object>();
                                    SalaryDetails.put("allowenceId", (String) allowenceGradeList.get(i).get("allowenceId"));
                                    factorId = (String) allowenceGradeList.get(i).get("factorId");
                                    if (factorId.equals("SAL_INCREMENT")) {
                                        amount = ((BigDecimal) allowenceGradeList.get(i).get("amount"));
                                    } else {
                                        amount = ((BigDecimal) allowenceGradeList.get(i).get("amount")).multiply(new BigDecimal(-1));
                                    }
                                    dayOfStartAllow = allowenceStartDate.toLocalDate().getDayOfMonth();
                                    dayOfLastTrans = LocalTransDate.toLocalDate().getDayOfMonth();
                                    //NumOfDaysGrade = kam yom btla3 25od fe had el month ya3ni eza kan el start 
//                                allowenceGrade fe 1/3/2017 w ana bde 27seb month 5 fbtltl3i 25od 30 yom kamlat ok ?
                                    System.out.println("numOfDaysGrade " + numOfDaysGrade);
                                    if (numOfDaysGrade == lengthOfMonth || dayOfLastTrans >= dayOfStartAllow) {
                                        totalAmount = (amount.doubleValue() / lengthOfMonthForCalculation) * nomOfDays;
                                        SalaryDetails.put("numOfDays", new Long(nomOfDays));
                                    } else if (numOfDaysGrade != lengthOfMonth) {
                                        totalAmount = (amount.doubleValue() / lengthOfMonthForCalculation) * numOfDaysGrade;
                                        SalaryDetails.put("numOfDays", new Long(numOfDaysGrade));

                                    }
                                    roundAmount = new BigDecimal(totalAmount);
                                    roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);
                                    SalaryDetails.put("degreeId", degreeId);
                                    SalaryDetails.put("amount", roundAmount.doubleValue());
                                    SalaryDetails.put("gradeId", gradeId);
                                    SalaryDetails.put("startDate", allowenceStartDate);
                                    SalaryDetails.put("endDate", (java.sql.Date) allowenceGradeList.get(i).get("endDate"));
                                    SalaryDetails.put("partyIdFrom", (String) row.get("partyIdFrom"));
                                    SalaryDetails.put("jobGroupId", (String) row.get("jobGroupId"));
                                    SalaryDetails.put("positionId", (String) row.get("emplPositionId"));
                                    SalaryDetails.put("description", (String) allowenceGradeList.get(i).get("description"));

                                    allowenceGrade.add(SalaryDetails);
                                }

                                allAllowencesGrade.add(allowenceGrade);

                            }
                        }
                    } else if (LocalTransDate.toLocalDate().isEqual(startCalculationDate)) {
                        transType = (String) row.get("TransType");
                        degreeId = (String) row.get("degreeId");
                        gradeId = (String) row.get("gradeId");
                        if (transType.equals("HOLD") && endDate == null) {
                            break;
                        } else {
                            if (transType.equals("HOLD") && endDate != null) {
                                nomOfDays = (previousTransDay - endDate.toLocalDate().getDayOfMonth()) - 1;
                                previousTransDay = lastDay;
                            } else if (transType.equals("TERMINATED")) {
                                nomOfDays = 1;
                            } else if (preiviousTransType.equals("TERMINATED")) {
                                nomOfDays = lastDay;
                            } else {
                                nomOfDays = lastDay - 1;
                            }
                            allowenceGradeList = getAllowenceGradeData_SalaryCalculation(request, gradeId);
                            System.out.println("allowenceGradeList " + gradeId + " " + allowenceGradeList);
                            if (preivousGradeId.equals(gradeId)) {
                                allowenceGradeList = getAllowenceGradeData_SalaryCalculation(request, preivousGradeId);
                                if (!allowenceGradeList.isEmpty()) {
                                    allowenceGrade = new ArrayList();
                                    for (int i = 0; i < allowenceGradeList.size(); i++) {
                                        SalaryDetails = new HashMap<String, Object>();
                                        allowenceStartDate = (java.sql.Date) allowenceGradeList.get(i).get("startDate");
                                        numOfDaysGrade = getNoOFDays_SalaryCalculation(allowenceStartDate, null, startCalculationDate, endCalculationDate);
                                        SalaryDetails = new HashMap<String, Object>();
                                        factorId = (String) allowenceGradeList.get(i).get("factorId");
                                        if (factorId.equals("SAL_INCREMENT")) {
                                            amount = ((BigDecimal) allowenceGradeList.get(i).get("amount"));
                                        } else {
                                            amount = ((BigDecimal) allowenceGradeList.get(i).get("amount")).multiply(new BigDecimal(-1));
                                        }
                                        dayOfStartAllow = allowenceStartDate.toLocalDate().getDayOfMonth();
                                        dayOfLastTrans = LocalTransDate.toLocalDate().getDayOfMonth();
                                        if (numOfDaysGrade == lengthOfMonth || dayOfLastTrans >= dayOfStartAllow) {
                                            totalAmount = amount.doubleValue();
                                            SalaryDetails.put("numOfDays", new Long(nomOfDays));

                                        } else if (numOfDaysGrade != lengthOfMonth) {
                                            totalAmount = (amount.doubleValue() / lengthOfMonthForCalculation) * numOfDaysGrade;
                                            SalaryDetails.put("numOfDays", new Long(numOfDaysGrade));
                                        }
                                        roundAmount = new BigDecimal(totalAmount);
                                        roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);
                                        SalaryDetails.put("degreeId", degreeId);
                                        SalaryDetails.put("amount", roundAmount.doubleValue());
                                        SalaryDetails.put("gradeId", gradeId);
                                        SalaryDetails.put("allowenceId", (String) allowenceGradeList.get(i).get("allowenceId"));
                                        SalaryDetails.put("startDate", (java.sql.Date) allowenceGradeList.get(i).get("startDate"));
                                        SalaryDetails.put("endDate", (java.sql.Date) allowenceGradeList.get(i).get("endDate"));
                                        SalaryDetails.put("partyIdFrom", (String) row.get("partyIdFrom"));
                                        SalaryDetails.put("jobGroupId", (String) row.get("jobGroupId"));
                                        SalaryDetails.put("positionId", (String) row.get("emplPositionId"));
                                        SalaryDetails.put("description", (String) allowenceGradeList.get(i).get("description"));

                                        allowenceGrade.add(SalaryDetails);

                                    }
//                                allAllowencesGrade.clear();
                                    allAllowencesGrade.add(allowenceGrade);
                                    break;
                                }
                            } else {
                                if (!allowenceGradeList.isEmpty()) {
                                    allowenceGrade = new ArrayList();
                                    for (int i = 0; i < allowenceGradeList.size(); i++) {
                                        SalaryDetails = new HashMap<String, Object>();
                                        allowenceStartDate = (java.sql.Date) allowenceGradeList.get(i).get("startDate");
                                        numOfDaysGrade = getNoOFDays_SalaryCalculation(allowenceStartDate, (java.sql.Date) null, startCalculationDate, endCalculationDate);
                                        SalaryDetails = new HashMap<String, Object>();

                                        factorId = (String) allowenceGradeList.get(i).get("factorId");
                                        if (factorId.equals("SAL_INCREMENT")) {
                                            amount = ((BigDecimal) allowenceGradeList.get(i).get("amount"));
                                        } else {
                                            amount = ((BigDecimal) allowenceGradeList.get(i).get("amount")).multiply(new BigDecimal(-1));
                                        }
                                        dayOfStartAllow = allowenceStartDate.toLocalDate().getDayOfMonth();
                                        dayOfLastTrans = LocalTransDate.toLocalDate().getDayOfMonth();
                                        if (numOfDaysGrade == lengthOfMonth || dayOfLastTrans >= dayOfStartAllow) {
                                            totalAmount = (amount.doubleValue() / lengthOfMonthForCalculation) * nomOfDays;
                                            SalaryDetails.put("numOfDays", new Long(nomOfDays));
                                        } else if (numOfDaysGrade != lengthOfMonth) {
                                            totalAmount = (amount.doubleValue() / lengthOfMonthForCalculation) * numOfDaysGrade;
                                            SalaryDetails.put("numOfDays", new Long(numOfDaysGrade));
                                        }
                                        SalaryDetails.put("startDate", (java.sql.Date) allowenceGradeList.get(i).get("startDate"));
                                        SalaryDetails.put("endDate", (java.sql.Date) allowenceGradeList.get(i).get("endDate"));
                                        roundAmount = new BigDecimal(totalAmount);
                                        roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);
                                        SalaryDetails.put("degreeId", degreeId);
                                        SalaryDetails.put("amount", roundAmount.doubleValue());
                                        SalaryDetails.put("gradeId", gradeId);
                                        SalaryDetails.put("description", (String) allowenceGradeList.get(i).get("description"));
                                        SalaryDetails.put("allowenceId", (String) allowenceGradeList.get(i).get("allowenceId"));
                                        SalaryDetails.put("positionId", (String) row.get("emplPositionId"));
                                        SalaryDetails.put("partyIdFrom", (String) row.get("partyIdFrom"));
                                        SalaryDetails.put("jobGroupId", (String) row.get("jobGroupId"));

                                        allowenceGrade.add(SalaryDetails);

                                    }

                                    allAllowencesGrade.add(allowenceGrade);
                                    break;
                                }
                            }

                        }
                    } else if (LocalTransDate.toLocalDate().isBefore(startCalculationDate)) {
                        degreeId = (String) row.get("degreeId");
                        gradeId = (String) row.get("gradeId");
                        if (preiviousTransType.equals("TERMINATED") && lastDay == lengthOfMonth) {
                            nomOfDays = 30;
                            preiviousTransType = "";
                        } else if (preiviousTransType.equals("TERMINATED") && lastDay != lengthOfMonth) {
                            nomOfDays = lastDay;
                            preiviousTransType = "";
                        } else {
                            nomOfDays = lastDay - 1;
                        }
                        if (preivousGradeId.equals(gradeId)) {
                            allowenceGradeList = getAllowenceGradeData_SalaryCalculation(request, preivousGradeId);
                            if (!allowenceGradeList.isEmpty()) {
                                allowenceGrade = new ArrayList();
                                for (int i = 0; i < allowenceGradeList.size(); i++) {
                                    SalaryDetails = new HashMap<String, Object>();
                                    allowenceStartDate = (java.sql.Date) allowenceGradeList.get(i).get("startDate");
                                    numOfDaysGrade = getNoOFDays_SalaryCalculation(allowenceStartDate, (java.sql.Date) null, startCalculationDate, endCalculationDate);
                                    SalaryDetails = new HashMap<String, Object>();
                                    factorId = (String) allowenceGradeList.get(i).get("factorId");
                                    if (factorId.equals("SAL_INCREMENT")) {
                                        amount = ((BigDecimal) allowenceGradeList.get(i).get("amount"));
                                    } else {
                                        amount = ((BigDecimal) allowenceGradeList.get(i).get("amount")).multiply(new BigDecimal(-1));
                                    }
                                    dayOfStartAllow = allowenceStartDate.toLocalDate().getDayOfMonth();
                                    dayOfLastTrans = LocalTransDate.toLocalDate().getDayOfMonth();
                                    if (numOfDaysGrade == lengthOfMonth || dayOfLastTrans >= dayOfStartAllow) {
                                        totalAmount = (amount.doubleValue() / lengthOfMonthForCalculation) * nomOfDays;
                                        SalaryDetails.put("numOfDays", new Long(nomOfDays));
                                    } else if (numOfDaysGrade != lengthOfMonth) {
                                        totalAmount = (amount.doubleValue() / lengthOfMonthForCalculation) * numOfDaysGrade;
                                        SalaryDetails.put("numOfDays", new Long(numOfDaysGrade));
                                    }
                                    roundAmount = new BigDecimal(totalAmount);
                                    roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);
                                    SalaryDetails.put("degreeId", degreeId);
                                    SalaryDetails.put("amount", roundAmount.doubleValue());
                                    SalaryDetails.put("gradeId", gradeId);
                                    SalaryDetails.put("allowenceId", (String) allowenceGradeList.get(i).get("allowenceId"));
                                    SalaryDetails.put("startDate", (java.sql.Date) allowenceGradeList.get(i).get("startDate"));
                                    SalaryDetails.put("endDate", (java.sql.Date) allowenceGradeList.get(i).get("endDate"));
                                    SalaryDetails.put("partyIdFrom", (String) row.get("partyIdFrom"));
                                    SalaryDetails.put("jobGroupId", (String) row.get("jobGroupId"));
                                    SalaryDetails.put("positionId", (String) row.get("emplPositionId"));
                                    SalaryDetails.put("description", (String) allowenceGradeList.get(i).get("description"));

                                    allowenceGrade.add(SalaryDetails);

                                }
//                                allAllowencesGrade.clear();
                                allAllowencesGrade.add(allowenceGrade);
                                break;
                            }
                        } else {
                            nomOfDays = lastDay - 1;
                            allowenceGradeList = getAllowenceGradeData_SalaryCalculation(request, gradeId);
                            if (!allowenceGradeList.isEmpty()) {
                                allowenceGrade = new ArrayList();
                                for (int i = 0; i < allowenceGradeList.size(); i++) {
                                    SalaryDetails = new HashMap<String, Object>();
                                    allowenceStartDate = (java.sql.Date) allowenceGradeList.get(i).get("startDate");
                                    numOfDaysGrade = getNoOFDays_SalaryCalculation(allowenceStartDate, (java.sql.Date) null, startCalculationDate, endCalculationDate);
                                    SalaryDetails = new HashMap<String, Object>();
                                    factorId = (String) allowenceGradeList.get(i).get("factorId");
                                    if (factorId.equals("SAL_INCREMENT")) {
                                        amount = ((BigDecimal) allowenceGradeList.get(i).get("amount"));
                                    } else {
                                        amount = ((BigDecimal) allowenceGradeList.get(i).get("amount")).multiply(new BigDecimal(-1));
                                    }
                                    dayOfStartAllow = allowenceStartDate.toLocalDate().getDayOfMonth();
                                    dayOfLastTrans = LocalTransDate.toLocalDate().getDayOfMonth();
                                    if (numOfDaysGrade == lengthOfMonth || dayOfLastTrans >= dayOfStartAllow) {
                                        totalAmount = (amount.doubleValue() / lengthOfMonthForCalculation) * nomOfDays;
                                        SalaryDetails.put("numOfDays", new Long(nomOfDays));
                                    } else if (numOfDaysGrade != lengthOfMonth) {
                                        totalAmount = (amount.doubleValue() / lengthOfMonthForCalculation) * numOfDaysGrade;
                                        SalaryDetails.put("numOfDays", new Long(numOfDaysGrade));
                                    }
                                    roundAmount = new BigDecimal(totalAmount);
                                    roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);
                                    SalaryDetails.put("degreeId", degreeId);
                                    SalaryDetails.put("amount", roundAmount.doubleValue());
                                    SalaryDetails.put("gradeId", gradeId);
                                    SalaryDetails.put("allowenceId", (String) allowenceGradeList.get(i).get("allowenceId"));
                                    SalaryDetails.put("startDate", (java.sql.Date) allowenceGradeList.get(i).get("startDate"));
                                    SalaryDetails.put("endDate", (java.sql.Date) allowenceGradeList.get(i).get("endDate"));
                                    SalaryDetails.put("partyIdFrom", (String) row.get("partyIdFrom"));
                                    SalaryDetails.put("jobGroupId", (String) row.get("jobGroupId"));
                                    SalaryDetails.put("positionId", (String) row.get("emplPositionId"));
                                    SalaryDetails.put("description", (String) allowenceGradeList.get(i).get("description"));

                                    allowenceGrade.add(SalaryDetails);

                                }

                                allAllowencesGrade.add(allowenceGrade);
                                break;
                            }
                        }
                    }
                }
                return allAllowencesGrade;
            } else {
                return allAllowencesGrade;
            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allAllowencesGrade;
    }

    /**
     * * Please read this document before read the code below ,
     * calculationMonth and calculationYear is the month and year user want to
     * calculate it startCalculationDate = start day for the month example user
     * send month 10 and year 2017 so startCalculationDate = 1/10/2017
     * lenghtOfMonth = number of days in month example month 10 = 31
     * endCalculationDate = end day for the month example user send month 10 and
     * year 2017 so endCalculationDate = 31/10/2017
     * <p>
     * <p>
     * <p>
     * Ahamd Rbab'ah *
     */
    public static String SalaryCalculation(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String jobGroupId = "";
        int numOfDays = 0;
        Map<String, String> messageMap;
        BigDecimal amount = new BigDecimal(0);
        BigDecimal roundAmount = new BigDecimal(0);
        int numberOfDecimal = getDecimalFormat_BasicSalary(request);
        if (numberOfDecimal == -1) {
            messageMap = UtilMisc.toMap("errorMessage", "success");
            String errMsg = UtilProperties.getMessage(resourceError, "NumberOfDecimal", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }
        String glAccountId = "";
        double totalAmount = 0.0;
        String userLoginId = getUserLoginId(request);
        PrintWriter out = null;
        String calculationMonth = request.getParameter("month");
        String calculationYear = request.getParameter("year");
        int calculationMonthValue = Integer.valueOf(calculationMonth);
        int calculationYearValue = Integer.valueOf(calculationYear);

        LocalDate startCalculationMonth = LocalDate.of(calculationYearValue, calculationMonthValue, 1);

//        to get Calculation Way either per 30 day or length of month
        String calculationWay = getGlobalPayrollSetting(request, "SALARY_CALCULATION_WAY");
        System.out.println("calculationWay " + calculationWay);

//        VALIDATION: check if the user select calculation way or not 
        if (calculationWay.equals("")) {
            messageMap = UtilMisc.toMap("errorMessage", "success");
            String errMsg = UtilProperties.getMessage(resourceError, "selectCalculationWay", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";

        }
        int lengthOfMonthForCalculation = 0;
        int lengthOfMonth = startCalculationMonth.lengthOfMonth();
        if (calculationWay.equals("LENGTH_OF_MONTH")) {
            lengthOfMonthForCalculation = startCalculationMonth.lengthOfMonth();
        } else if (calculationWay.equals("THIRTY_DAYS")) {
            lengthOfMonthForCalculation = 30;
        }

        LocalDate endCalculationMonth = LocalDate.of(calculationYearValue, calculationMonthValue, lengthOfMonth);
        GenericValue gvValue = delegator.makeValue("EmplSalary");
        java.sql.Date localEndDate = null;
        Map<String, Object> BasicSalaryDetials;
        Map<String, Object> EmplDetials;
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("EmplStatus", "HIRED");
        String partyId = "";
        java.sql.Date lastTransDate = null;
        java.sql.Date hiringDate = null;
        Map<String, Object> criteriaMap = new HashMap<String, Object>();
        criteriaMap.put("year", new Long(calculationYearValue));
        criteriaMap.put("month", new Long(calculationMonth));
        criteriaMap.put("paymentTypeId", "PAY_TYPE_CASH");
        boolean beganTransaction = false;
        String emplStatus = "";
        boolean isTerminated_endMonth = false;
        boolean isTerminated_startMonth = false;
        boolean isHold_startDate = false;
        boolean isEmployeeTerminate = false;
        EmployeePosition employeePosition = null;
        try {

//             VALIDATION: check if this month is posted 
            if (isPosted_Totaly(request, new Long(calculationYearValue), new Long(calculationMonth))) {

                messageMap = UtilMisc.toMap("errorMessage", "Posted");
                String errMsg = UtilProperties.getMessage(resourceError, "Posted", messageMap, UtilHttp.getLocale(request));
                request.setAttribute("_ERROR_MESSAGE_", errMsg);
                return "error";
//                
            }
            beganTransaction = TransactionUtil.begin();
            int removeAll = delegator.removeByAnd("EmplSalary", criteriaMap);

            // get all employee HIRED from Person Entity            
            List<GenericValue> salaryGradeList = delegator.findList("Person", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue genericValue : salaryGradeList) {
                partyId = (String) genericValue.get("partyId");
                isEmployeeTerminate = PersonUtilServices.isTerminated_Employee_ByMonth(request, partyId, startCalculationMonth, endCalculationMonth);
                if (isEmployeeTerminate) {
                    continue;
                }
//get LastTrans Type for employee  to check if the employee terminated or hold  or no ex :
// want to calculate month 10 and lastTrans Type in month 9 was be TERMINATED 
//so this service return TERMINATED Type so the employee dosen't have Salary in month 10 chekc from 1/10 to 31/10
                emplStatus = PersonUtilServices.isTerminated_isHOLD_Employee_ByMonth(request, partyId, startCalculationMonth, endCalculationMonth);

                isHold_startDate = PersonUtilServices.isExsistTransType_ByTransDate(request, partyId, java.sql.Date.valueOf(startCalculationMonth), "HOLD");
                boolean isHold =PersonUtilServices.isHoldEmployee(request,partyId,java.sql.Date.valueOf(startCalculationMonth));
                System.out.println("emplStatus from SalaryCalcultion for partyId " + partyId + " is " + emplStatus + "  and  isHold in start month  ?" + isHold_startDate);

                if ((!emplStatus.equals("HOLD") && !emplStatus.equals("TERMINATED")) && !isHold_startDate ) {

// get lastActive Transaction Date from EmplSalaryGrade Entity for each employee
                    lastTransDate = getLastActive_TransDate_Employee_SalaryCalculation(request, partyId, calculationMonthValue, calculationYearValue);
                    hiringDate = getHiredDate(request, partyId);
                    System.out.println("hiringDate from Salary " + hiringDate);
                    if (lastTransDate != null && hiringDate != null) {
                        // get map from service below that contains  degreeId GradeId jobgroupId employment and BasicSalary
                        employeePosition = PersonUtilServices.getEmployeePositionDetails(request, partyId, calculationMonthValue, calculationYearValue);
//                get Number of working days for each employee in this month based on lastTransDate
//          if emplStatus is Terminated in started month ex 31/1/2017 status Terminated
                        isTerminated_endMonth = PersonUtilServices.isExsistTransType_ByTransDate(request, partyId, java.sql.Date.valueOf(endCalculationMonth), "TERMINATED");

//          if emplStatus is Terminated in started month ex 01/1/2017 status Terminated so noOfDay =1
                        isTerminated_startMonth = PersonUtilServices.isExsistTransType_ByTransDate(request, partyId, java.sql.Date.valueOf(startCalculationMonth), "TERMINATED");

                        System.out.println(" partyId and Terminated and Trans Date " + partyId + " " + isTerminated_endMonth + " isTerminated_startMonth " + isTerminated_startMonth + " transDate" + lastTransDate);

//                        if (isTerminated_endMonth) {
//                            numOfDays = lengthOfMonth;
//                        } else 
                        if (isTerminated_startMonth) {
                            numOfDays = 1;
                        }else if(isHold){
                            numOfDays=10;
                        }
                        else {
                            numOfDays = getNoOFDays_SalaryCalculation(lastTransDate, null, startCalculationMonth, endCalculationMonth);
                        }

                        System.out.println("numOfDays TEr " + numOfDays);
                        if (numOfDays == lengthOfMonth) {
                            amount = employeePosition.getBasicSalary();
                            totalAmount = amount.doubleValue();
                            gvValue.set("emplSalaryId", delegator.getNextSeqId("emplSalaryId"));
                            gvValue.set("partyId", partyId);
                            gvValue.set("gradeId", employeePosition.getGradeId());
                            gvValue.set("degreeId", employeePosition.getDegreeId());
                            gvValue.set("jobGroupId", employeePosition.getJobGroupId());
                            gvValue.set("organizationPartyId", employeePosition.getPartyIdFrom());
                            gvValue.set("allowenceId", (String) getAlloweceIdForBasicSalary(request));
                            gvValue.set("amountType", "الراتب الأساسي");
                            gvValue.set("positionId", employeePosition.getEmplPositionId());
                            glAccountId = getGlAccountId(request, (String) getAlloweceIdForBasicSalary(request), "Emp");
                            gvValue.set("glAccountId", glAccountId);
                            gvValue.set("month", new Long(calculationMonthValue));
                            gvValue.set("year", new Long(calculationYearValue));
                            gvValue.set("amount", totalAmount);
                            gvValue.set("nomDay", new Long(30));
                            gvValue.set("userLoginId", userLoginId);
                            gvValue.set("isCompany", "N");
                            gvValue.set("paymentTypeId", "PAY_TYPE_CASH");

                            gvValue.set("isPosted", "N");
                            gvValue.create();

                        } else if (lengthOfMonth != numOfDays ) {
                            List<Map> salaryDetails = getPreviousBasicSalary_SalaryCalculation(request, (String) genericValue.get("partyId"), lengthOfMonthForCalculation, numberOfDecimal, startCalculationMonth, endCalculationMonth);
                            for (int i = 0; i < salaryDetails.size(); i++) {
                                gvValue.set("emplSalaryId", delegator.getNextSeqId("emplSalaryId"));
                                gvValue.set("partyId", genericValue.get("partyId"));
                                gvValue.set("gradeId", salaryDetails.get(i).get("gradeId"));
                                gvValue.set("degreeId", salaryDetails.get(i).get("degreeId"));
                                gvValue.set("jobGroupId", salaryDetails.get(i).get("jobGroupId"));
                                gvValue.set("organizationPartyId", salaryDetails.get(i).get("partyIdFrom"));
                                gvValue.set("allowenceId", (String) getAlloweceIdForBasicSalary(request));
                                gvValue.set("allowenceId", (String) getAlloweceIdForBasicSalary(request));
                                glAccountId = getGlAccountId(request, (String) getAlloweceIdForBasicSalary(request), "Emp");
                                gvValue.set("glAccountId", glAccountId);
                                gvValue.set("amountType", "الراتب الأساسي");
                                gvValue.set("positionId", salaryDetails.get(i).get("positionId"));
                                gvValue.set("month", new Long(calculationMonthValue));
                                gvValue.set("year", new Long(calculationYearValue));
                                gvValue.set("amount", salaryDetails.get(i).get("basicSalary"));
                                gvValue.set("nomDay", salaryDetails.get(i).get("numOfDays"));
                                gvValue.set("userLoginId", userLoginId);
                                gvValue.set("isCompany", "N");
                                gvValue.set("isPosted", "N");
                                gvValue.set("paymentTypeId", "PAY_TYPE_CASH");
                                gvValue.create();
                            }
                        }

                    }

                }
            }
            //            for Allowence Grad 
            java.sql.Date allowenceStartDate = null;
            java.sql.Date allowenceEndDate = null;
            int setTax = 0;
            String factorId = "";
            for (GenericValue genericValue : salaryGradeList) {
                partyId = (String) genericValue.get("partyId");
                isEmployeeTerminate = PersonUtilServices.isTerminated_Employee_ByMonth(request, partyId, startCalculationMonth, endCalculationMonth);
                if (isEmployeeTerminate) {
                    continue;
                }
                emplStatus = PersonUtilServices.isTerminated_isHOLD_Employee_ByMonth(request, partyId, startCalculationMonth, endCalculationMonth);
                System.out.println("emplStatus from AllowenceGrade " + emplStatus + " " + partyId);
                isHold_startDate = PersonUtilServices.isExsistTransType_ByTransDate(request, partyId, java.sql.Date.valueOf(startCalculationMonth), "HOLD");

                if ((!emplStatus.equals("HOLD") && !emplStatus.equals("TERMINATED")) && !isHold_startDate) {
                    setTax = IncomeTaxServices.IncomeTaxService(request, partyId, calculationMonthValue, calculationYearValue);
                    // get lastActive Transaction Date from EmplSalaryGrade Entity for each employee
                    lastTransDate = getLastActive_TransDate_Employee_SalaryCalculation(request, partyId, calculationMonthValue, calculationYearValue);
                    hiringDate = getHiredDate(request, partyId);
                    System.out.println("hiringDate from Allowences " + hiringDate);
                    if (lastTransDate != null && hiringDate != null) {

                        // get map from service below that contains  jobGroupId GradeId And Department 
                        employeePosition = PersonUtilServices.getEmployeePositionDetails(request, partyId, calculationMonthValue, calculationYearValue);

                        //get Number of working days for each employee in this month based on lastTransDate
//                   numOfDays = getNoOFDays_SalaryCalculation(lastTransDate, (java.sql.Date) null, calculationMonthValue, calculationYearValue);
                        isTerminated_endMonth = PersonUtilServices.isExsistTransType_ByTransDate(request, partyId, java.sql.Date.valueOf(endCalculationMonth), "TERMINATED");

//          if emplStatus is Terminated in started month ex 01/1/2017 status Terminated so noOfDay =1
                        isTerminated_startMonth = PersonUtilServices.isExsistTransType_ByTransDate(request, partyId, java.sql.Date.valueOf(startCalculationMonth), "TERMINATED");

                        System.out.println(" From Allowence Grade partyId and Terminated and Trans Date " + partyId + " " + isTerminated_endMonth + " isTerminated_startMonth " + isTerminated_startMonth + " transDate" + lastTransDate);

//                        if (isTerminated_endMonth) {
//                            numOfDays = lengthOfMonth;
//                        } else
                        if (isTerminated_startMonth) {
                            numOfDays = 1;
                        } else {
                            numOfDays = getNoOFDays_SalaryCalculation(lastTransDate, null, startCalculationMonth, endCalculationMonth);
                        }
                        System.out.println("numOfDays from AllowenceGrade " + numOfDays + " and party " + partyId);
                        List<Map> AllownceGradeList = getAllowenceGradeData_SalaryCalculation(request, (String) employeePosition.getGradeId());
                        if (numOfDays == lengthOfMonth) {
                            for (int i = 0; i < AllownceGradeList.size(); i++) {
                                allowenceStartDate = (java.sql.Date) AllownceGradeList.get(i).get("startDate");
                                allowenceEndDate = (java.sql.Date) AllownceGradeList.get(i).get("endDate");

                                if (allowenceEndDate == null || endCalculationMonth.isBefore(allowenceEndDate.toLocalDate()) || endCalculationMonth.isEqual(allowenceEndDate.toLocalDate())) {
                                    int numOfDaysGrade = getNoOFDays_SalaryCalculation(allowenceStartDate, (java.sql.Date) null, startCalculationMonth, endCalculationMonth);
                                    if (allowenceStartDate.toLocalDate().isBefore(startCalculationMonth) || allowenceStartDate.toLocalDate().isEqual(startCalculationMonth)
                                            || allowenceStartDate.toLocalDate().isBefore(endCalculationMonth) || allowenceStartDate.toLocalDate().isEqual(endCalculationMonth)) {
                                        gvValue.set("emplSalaryId", delegator.getNextSeqId("emplSalaryId"));
                                        gvValue.set("partyId", partyId);
                                        gvValue.set("gradeId", employeePosition.getGradeId());
                                        gvValue.set("degreeId", "المرتبة");
                                        gvValue.set("jobGroupId", employeePosition.getJobGroupId());
                                        gvValue.set("organizationPartyId", employeePosition.getPartyIdFrom());
                                        gvValue.set("allowenceId", AllownceGradeList.get(i).get("allowenceId"));
                                        glAccountId = getGlAccountId(request, (String) AllownceGradeList.get(i).get("allowenceId"), "Emp");
                                        gvValue.set("glAccountId", glAccountId);
                                        gvValue.set("month", new Long(calculationMonthValue));
                                        gvValue.set("year", new Long(calculationYearValue));
                                        gvValue.set("amountType", "بدل المرتبة");
                                        gvValue.set("positionId", "-");
                                        gvValue.set("userLoginId", userLoginId);
                                        gvValue.set("isPosted", "N");
                                        gvValue.set("paymentTypeId", "PAY_TYPE_CASH");
                                        gvValue.set("isCompany", "N");
                                        factorId = getAllowenceFactorId(request, (String) AllownceGradeList.get(i).get("allowenceId"));
                                        if (factorId.equals("SAL_INCREMENT")) {
                                            amount = ((BigDecimal) AllownceGradeList.get(i).get("amount"));
                                        } else {
                                            amount = ((BigDecimal) AllownceGradeList.get(i).get("amount")).multiply(new BigDecimal(-1));
                                        }
                                        if (numOfDaysGrade == lengthOfMonth) {

                                            roundAmount = new BigDecimal(amount.doubleValue());
                                            roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);
                                            gvValue.set("amount", roundAmount.doubleValue());
                                            gvValue.set("nomDay", new Long(30));
                                        } else if (numOfDaysGrade != lengthOfMonth) {
                                            totalAmount = (amount.doubleValue() / lengthOfMonthForCalculation) * numOfDaysGrade;
                                            roundAmount = new BigDecimal(totalAmount);
                                            roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);
                                            gvValue.set("amount", roundAmount.doubleValue());

                                            gvValue.set("nomDay", new Long(numOfDaysGrade));
                                        }
                                        gvValue.create();
                                    }
                                }

                            }

                        } else if (isTerminated_startMonth) {
                            for (int i = 0; i < AllownceGradeList.size(); i++) {
                                allowenceStartDate = (java.sql.Date) AllownceGradeList.get(i).get("startDate");
                                allowenceEndDate = (java.sql.Date) AllownceGradeList.get(i).get("endDate");

                                if (allowenceEndDate == null || endCalculationMonth.isBefore(allowenceEndDate.toLocalDate()) || endCalculationMonth.isEqual(allowenceEndDate.toLocalDate())) {
///                                        int numOfDaysGrade = getNoOFDays_SalaryCalculation(allowenceStartDate, (java.sql.Date) null, calculationMonthValue, calculationYearValue);
                                    if (allowenceStartDate.toLocalDate().isBefore(startCalculationMonth) || allowenceStartDate.toLocalDate().isEqual(startCalculationMonth)
                                            || allowenceStartDate.toLocalDate().isBefore(endCalculationMonth) || allowenceStartDate.toLocalDate().isEqual(endCalculationMonth)) {
                                        gvValue.set("emplSalaryId", delegator.getNextSeqId("emplSalaryId"));
                                        gvValue.set("partyId", partyId);
                                        gvValue.set("gradeId", employeePosition.getGradeId());
                                        gvValue.set("degreeId", "المرتبة");
                                        gvValue.set("jobGroupId", employeePosition.getJobGroupId());
                                        gvValue.set("organizationPartyId", employeePosition.getPartyIdFrom());
                                        gvValue.set("allowenceId", AllownceGradeList.get(i).get("allowenceId"));
                                        glAccountId = getGlAccountId(request, (String) AllownceGradeList.get(i).get("allowenceId"), "Emp");
                                        gvValue.set("glAccountId", glAccountId);
                                        gvValue.set("month", new Long(calculationMonthValue));
                                        gvValue.set("year", new Long(calculationYearValue));
                                        gvValue.set("amountType", "بدل المرتبة");
                                        gvValue.set("positionId", "-");
                                        gvValue.set("userLoginId", userLoginId);
                                        gvValue.set("isPosted", "N");
                                        gvValue.set("isCompany", "N");
                                        gvValue.set("paymentTypeId", "PAY_TYPE_CASH");
                                        factorId = getAllowenceFactorId(request, (String) AllownceGradeList.get(i).get("allowenceId"));
                                        if (factorId.equals("SAL_INCREMENT")) {
                                            amount = ((BigDecimal) AllownceGradeList.get(i).get("amount"));
                                        } else {
                                            amount = ((BigDecimal) AllownceGradeList.get(i).get("amount")).multiply(new BigDecimal(-1));
                                        }
//                                            if (numOfDaysGrade == lengthOfMonth) {
//
//                                                roundAmount = new BigDecimal(amount.doubleValue());
//                                                roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);
//                                                gvValue.set("amount", roundAmount.doubleValue());
//                                                gvValue.set("nomDay", new Long(30));
//                                            } else if (numOfDaysGrade != lengthOfMonth) {
                                        totalAmount = (amount.doubleValue() / lengthOfMonthForCalculation);
                                        roundAmount = new BigDecimal(totalAmount);
                                        roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);
                                        gvValue.set("amount", roundAmount.doubleValue());

                                        gvValue.set("nomDay", new Long(1));
//                                            }
                                        gvValue.create();
                                    }
                                }

                            }

                        } else if (numOfDays != lengthOfMonth) {
                            System.out.println("I'm here " + partyId);
                            List<List<Map>> AllowenceGradeListPrev = getPreviousAllowenceGrade_SalaryCalculation(request, (String) genericValue.get("partyId"), lengthOfMonthForCalculation, numberOfDecimal, startCalculationMonth, endCalculationMonth);
                            System.out.println("AllowenceGradeListPrev " + AllowenceGradeListPrev);
                            System.out.println("AllowenceGradeListPrev Size " + AllowenceGradeListPrev.size());
                            for (int i = 0; i < AllowenceGradeListPrev.size(); i++) {
                                for (int j = 0; j < AllowenceGradeListPrev.get(i).size(); j++) {
                                    allowenceStartDate = (java.sql.Date) AllowenceGradeListPrev.get(i).get(j).get("startDate");
                                    allowenceEndDate = (java.sql.Date) AllowenceGradeListPrev.get(i).get(j).get("endDate");
                                    if (allowenceEndDate == null || endCalculationMonth.isBefore(allowenceEndDate.toLocalDate()) || endCalculationMonth.isEqual(allowenceEndDate.toLocalDate())) {
                                        if (allowenceStartDate.toLocalDate().isBefore(startCalculationMonth) || allowenceStartDate.toLocalDate().isEqual(startCalculationMonth) || allowenceStartDate.toLocalDate().isBefore(endCalculationMonth)) {
                                            gvValue.set("emplSalaryId", delegator.getNextSeqId("emplSalaryId"));
                                            gvValue.set("partyId", partyId);
                                            gvValue.set("gradeId", AllowenceGradeListPrev.get(i).get(j).get("gradeId"));
                                            gvValue.set("degreeId", "-");
                                            gvValue.set("jobGroupId", employeePosition.getJobGroupId());
                                            gvValue.set("amountType", "بدل المرتبة");
                                            gvValue.set("positionId", "-");
                                            gvValue.set("organizationPartyId", employeePosition.getPartyIdFrom());
                                            gvValue.set("allowenceId", AllowenceGradeListPrev.get(i).get(j).get("allowenceId"));
                                            glAccountId = getGlAccountId(request, (String) AllowenceGradeListPrev.get(i).get(j).get("allowenceId"), "Emp");
                                            gvValue.set("glAccountId", glAccountId);
                                            gvValue.set("month", new Long(calculationMonthValue));
                                            gvValue.set("year", new Long(calculationYearValue));
                                            gvValue.set("userLoginId", userLoginId);
                                            gvValue.set("amount", AllowenceGradeListPrev.get(i).get(j).get("amount"));
                                            gvValue.set("nomDay", AllowenceGradeListPrev.get(i).get(j).get("numOfDays"));
                                            gvValue.set("isPosted", "N");
                                            gvValue.set("isCompany", "N");
                                            gvValue.set("paymentTypeId", "PAY_TYPE_CASH");
                                            gvValue.create();
                                        }
                                    }
                                }

                            }
                        }

                    }

                }
            }

//            for Allowence Employee 
            String allowenceRelated = "";
            String sslAllowenceId ="";
            int numOfDaysTotaly =0;
            List<GenericValue> allownceEmployeeList = delegator.findAll("EmpAllowencesView", true);

            for (GenericValue genericValue : allownceEmployeeList) {

                partyId = genericValue.get("partyId").toString();
                isEmployeeTerminate = PersonUtilServices.isTerminated_Employee_ByMonth(request, partyId, startCalculationMonth, endCalculationMonth);
                if (isEmployeeTerminate) {
                    continue;
                }
                emplStatus = PersonUtilServices.isTerminated_isHOLD_Employee_ByMonth(request, partyId, startCalculationMonth, endCalculationMonth);
                isHold_startDate = PersonUtilServices.isExsistTransType_ByTransDate(request, partyId, java.sql.Date.valueOf(startCalculationMonth), "HOLD");

                if ((!emplStatus.equals("HOLD") && !emplStatus.equals("TERMINATED")) && !isHold_startDate) {

                    isTerminated_endMonth = PersonUtilServices.isExsistTransType_ByTransDate(request, partyId, java.sql.Date.valueOf(endCalculationMonth), "TERMINATED");

//          if emplStatus is Terminated in started month ex 01/1/2017 status Terminated so noOfDay =1
                    isTerminated_startMonth = PersonUtilServices.isExsistTransType_ByTransDate(request, partyId, java.sql.Date.valueOf(startCalculationMonth), "TERMINATED");

                    jobGroupId = getJobGroupId(request, partyId);

                    hiringDate = getHiredDate(request, partyId);
                    numOfDaysTotaly =getNoOFDays_SalaryCalculation(null,null,hiringDate.toLocalDate(),null);
                    System.out.println("numOfDaysTotaly "+numOfDaysTotaly);
                    java.sql.Date AllowenceStartDate = (java.sql.Date) genericValue.get("startDate");
                    if (AllowenceStartDate.toLocalDate().isEqual(hiringDate.toLocalDate()) || AllowenceStartDate.toLocalDate().isAfter(hiringDate.toLocalDate())) {

                        if (genericValue.get("endDate") != null) {
                            localEndDate = (java.sql.Date) genericValue.get("endDate");
                        } else {
                            localEndDate = null;
                        }
                        if (genericValue.get("endDate") == null || endCalculationMonth.isBefore(localEndDate.toLocalDate()) || endCalculationMonth.isEqual(localEndDate.toLocalDate())) {
                            if (endCalculationMonth.isAfter(AllowenceStartDate.toLocalDate()) || endCalculationMonth.isEqual(AllowenceStartDate.toLocalDate())) {
                                if (genericValue.get("endDate") == null) {
                                    numOfDays = getNoOFDays_SalaryCalculation((java.sql.Date) genericValue.get("startDate"), (java.sql.Date) null, startCalculationMonth, endCalculationMonth);
                                } else if (genericValue.get("endDate") != null) {
                                    numOfDays = getNoOFDays_SalaryCalculation((java.sql.Date) genericValue.get("startDate"), (java.sql.Date) genericValue.get("endDate"), startCalculationMonth, endCalculationMonth);
                                }
                                if (genericValue.get("factorId").equals("SAL_INCREMENT")) {
                                    amount = ((BigDecimal) genericValue.get("amount"));
                                } else if (genericValue.get("factorId").equals("SAL_DEDUCT")) {
                                    amount = ((BigDecimal) genericValue.get("amount")).multiply(new BigDecimal(-1));
                                }
                                if (numOfDays != lengthOfMonth) {

                                    totalAmount = (amount.doubleValue() / lengthOfMonthForCalculation) * numOfDays;
                                } else if (numOfDays == lengthOfMonth) {

                                    numOfDays = 30;
                                    totalAmount = amount.doubleValue();
                                }
                                roundAmount = new BigDecimal(totalAmount);
                                roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);
                                gvValue.set("emplSalaryId", delegator.getNextSeqId("emplSalaryId"));
                                gvValue.set("partyId", genericValue.get("partyId"));
                                gvValue.set("gradeId", "-");
                                gvValue.set("degreeId", "-");
                                gvValue.set("jobGroupId", jobGroupId);
                                gvValue.set("organizationPartyId", (String) getDepartmentId(request, genericValue.get("partyId").toString(), calculationMonthValue, calculationYearValue));
                                gvValue.set("allowenceId", genericValue.get("allowenceId"));
                                allowenceRelated = (String) genericValue.get("typeId");
                                gvValue.set("paymentTypeId", "PAY_TYPE_CASH");
                                System.out.println("allowenceRelated " + allowenceRelated);
                                if (allowenceRelated.equals("COM")) {
                                    gvValue.set("amountType", "بدل على الشركة");
                                    gvValue.set("isCompany", "Y");
                                    glAccountId = getGlAccountId(request, (String) genericValue.get("allowenceId"), "Comp");
                                    gvValue.set("glAccountId", glAccountId);
                                } else if (allowenceRelated.equals("EMP")) {
                                    gvValue.set("amountType", "بدل الموظف");
                                    gvValue.set("isCompany", "N");
                                    glAccountId = getGlAccountId(request, (String) genericValue.get("allowenceId"), "Emp");
                                    gvValue.set("glAccountId", glAccountId);
                                }
                                gvValue.set("positionId", "-");

                                gvValue.set("amount", roundAmount.doubleValue());
                                gvValue.set("month", new Long(calculationMonthValue));
                                gvValue.set("year", new Long(calculationYearValue));
                                gvValue.set("nomDay", new Long(numOfDays));
                                gvValue.set("userLoginId", userLoginId);
                                gvValue.set("isPosted", "N");
                                gvValue.create();
                            }
                        } else if (endCalculationMonth.isAfter(localEndDate.toLocalDate())) {
                            int currentMonth = endCalculationMonth.getMonthValue();
                            int currentYear = endCalculationMonth.getYear();
                            int endMonth = localEndDate.toLocalDate().getMonthValue();
                            int endYear = localEndDate.toLocalDate().getYear();
                            if (endYear == currentYear && endMonth == currentMonth) {
                                gvValue.set("emplSalaryId", delegator.getNextSeqId("emplSalaryId"));
                                gvValue.set("partyId", genericValue.get("partyId"));
                                gvValue.set("gradeId", "-");
                                gvValue.set("degreeId", "-");
                                gvValue.set("jobGroupId", jobGroupId);
                                gvValue.set("organizationPartyId", "-");
                                gvValue.set("allowenceId", genericValue.get("allowenceId"));
                                glAccountId = getGlAccountId(request, (String) genericValue.get("allowenceId"), "Emp");
                                gvValue.set("glAccountId", glAccountId);
                                allowenceRelated = (String) genericValue.get("typeId");
                                System.out.println("allowenceRelated " + allowenceRelated);
                                if (allowenceRelated.equals("COM")) {
                                    gvValue.set("amountType", "بدل على الشركة");
                                    gvValue.set("isCompany", "Y");
                                    glAccountId = getGlAccountId(request, (String) genericValue.get("allowenceId"), "Comp");
                                    gvValue.set("glAccountId", glAccountId);
                                } else if (allowenceRelated.equals("EMP")) {
                                    gvValue.set("amountType", "بدل الموظف");
                                    gvValue.set("isCompany", "N");
                                    glAccountId = getGlAccountId(request, (String) genericValue.get("allowenceId"), "Emp");
                                    gvValue.set("glAccountId", glAccountId);
                                }
                                numOfDays = getNoOFDays_SalaryCalculation((java.sql.Date) genericValue.get("startDate"), (java.sql.Date) genericValue.get("endDate"), startCalculationMonth, endCalculationMonth);
                                if (genericValue.get("factorId").equals("SAL_INCREMENT")) {
                                    amount = ((BigDecimal) genericValue.get("amount"));
                                } else {
                                    amount = ((BigDecimal) genericValue.get("amount")).multiply(new BigDecimal(-1));
                                }
                                if (numOfDays != lengthOfMonth) {
                                    totalAmount = (amount.doubleValue() / lengthOfMonthForCalculation) * numOfDays;
                                } else {
                                    numOfDays = 30;
                                    totalAmount = amount.doubleValue();
                                }
                                roundAmount = new BigDecimal(totalAmount);
                                roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);
                                gvValue.set("amount", roundAmount.doubleValue());
                                gvValue.set("month", new Long(currentMonth));
                                gvValue.set("year", new Long(currentYear));
                                gvValue.set("nomDay", new Long(numOfDays));
                                gvValue.set("userLoginId", userLoginId);
                                gvValue.set("paymentTypeId", "PAY_TYPE_CASH");
                                gvValue.set("isPosted", "N");
                                gvValue.create();
                            }
                        }
                    }

                }
            }
//              
        } catch (Exception ex) {

            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
            try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
                messageMap = UtilMisc.toMap("errorMessage", ex.getMessage());
                String errMsg = UtilProperties.getMessage(resourceError, "", messageMap, UtilHttp.getLocale(request));
                request.setAttribute("_ERROR_MESSAGE_", ex.getMessage());
                return "error";

            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                return "error";
            }

        } finally {
            // only commit the transaction if we started one... this will throw an exception if it fails
            try {
                TransactionUtil.commit(beganTransaction);
                messageMap = UtilMisc.toMap("successMessage", "");

            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not commit transaction for entity engine error occurred while saving abandoned cart information", module);
                messageMap = UtilMisc.toMap("errorMessage", e.getMessage());
                String errMsg = UtilProperties.getMessage(resourceError, "", messageMap, UtilHttp.getLocale(request));
                request.setAttribute("_ERROR_MESSAGE_", errMsg);
                return "error";
            }
        }
        String successMsg = UtilProperties.getMessage(resourceError, "calculatedSuccessFully", messageMap, UtilHttp.getLocale(request));
        request.setAttribute("_EVENT_MESSAGE_", successMsg);
        return "success";

    }

    public static String getSalaryCalculationData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        Map<String, Object> criteria = new HashMap<String, Object>();

        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        String jobGroup = request.getParameter("jobGroup");
        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("jobGroupId", jobGroup);
        criteria.put("paymentTypeId", "PAY_TYPE_CASH");

        try {
            List<GenericValue> list = null;
            if (month == 0) {
                list = delegator.findAll("EmplSalary", true);
            } else {
                list = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
            }
            for (GenericValue row : list) {
                jsonRes = new JSONObject();
                String gradeId = (String) row.get("gradeId");
                jsonRes.put("emplSalaryId", row.get("emplSalaryId"));
                jsonRes.put("partyId", row.get("partyId"));
                jsonRes.put("fullName", HumanResEvents.getPartyName(request, response, (String) row.get("partyId")));
                jsonRes.put("jobGroupId", getJobGroupDescription(request, (String) row.get("jobGroupId")));
                jsonRes.put("organizationPartyId", (String) row.get("organizationPartyId"));
                jsonRes.put("allowenceId", getAllowenceDescription(request, (String) row.get("allowenceId")));
                jsonRes.put("gradeId", getGradeDescription(request, (String) row.get("gradeId")));
                jsonRes.put("degreeId", getDegreeDescription(request, (String) row.get("degreeId")));
                jsonRes.put("amountType", (String) row.get("amountType"));
                String positionTypeId = getEmplPositionTypeId(request, (String) row.get("positionId"));
                jsonRes.put("positionId", getEmplPositionTypeDescription(request, positionTypeId));
                jsonRes.put("amount", row.get("amount"));
                jsonRes.put("glAccountId", row.get("glAccountId"));
                jsonRes.put("year", row.get("year"));
                jsonRes.put("month", row.get("month"));
                jsonRes.put("nomDay", row.get("nomDay"));
                jsonRes.put("isPosted", row.get("isPosted"));
                jsonRes.put("isCompany", row.get("isCompany"));
                jsonData.add(jsonRes);
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String getSalaryCalculationData_Expense(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        Map<String, Object> criteria = new HashMap<String, Object>();

        String partyId = request.getParameter("partyId");
        String expenseType = request.getParameter("expenseType");
//        String year = request.getParameter("year");
//        String month = request.getParameter("month");
        String expenseId = request.getParameter("expenseId");

        if (!expenseId.isEmpty()) {
            System.out.println("*" + expenseId + "*");
            criteria.put("expenseId", expenseId);
        }

        criteria.put("partyId", partyId);
        criteria.put("expenseType", expenseType);
        criteria.put("paymentTypeId", "PAY_TYPE_SEPARATED");
        String isCompany = "";
        try {
            List<GenericValue> list = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
            System.out.println("***");
            System.out.println(list);
            if (list.size() != 0) {
                for (GenericValue row : list) {
                    isCompany = (String) row.get("isCompany");
                    if (!isCompany.equals("Y")) {
                        jsonRes = new JSONObject();
                        String gradeId = (String) row.get("gradeId");
                        jsonRes.put("emplSalaryId", row.get("emplSalaryId"));
                        jsonRes.put("partyId", row.get("partyId"));
                        jsonRes.put("fullName", HumanResEvents.getPartyName(request, response, (String) row.get("partyId")));
                        jsonRes.put("jobGroupId", getJobGroupDescription(request, (String) row.get("jobGroupId")));
                        jsonRes.put("organizationPartyId", (String) row.get("organizationPartyId"));//
                        jsonRes.put("allowenceId", getAllowenceDescription(request, (String) row.get("allowenceId")));
                        jsonRes.put("gradeId", getGradeDescription(request, (String) row.get("gradeId")));
                        jsonRes.put("degreeId", getDegreeDescription(request, (String) row.get("degreeId")));
                        jsonRes.put("amountType", (String) row.get("amountType"));
                        String positionTypeId = getEmplPositionTypeId(request, (String) row.get("positionId"));
                        jsonRes.put("positionId", getEmplPositionTypeDescription(request, positionTypeId));
                        jsonRes.put("amount", row.get("amount"));
                        jsonRes.put("glAccountId", row.get("glAccountId"));
                        jsonRes.put("year", row.get("year"));
                        jsonRes.put("month", row.get("month"));
                        jsonRes.put("nomDay", row.get("nomDay"));
                        jsonRes.put("isPosted", row.get("isPosted"));
                        jsonRes.put("isCompany", row.get("isCompany"));
                        jsonRes.put("expenseId", row.get("expenseId"));
                        jsonRes.put("expenseType", row.get("expenseType"));
                        jsonRes.put("paymentTypeId", row.get("paymentTypeId"));
                        jsonRes.put("urlAttachment", (String) row.get("urlAttachment"));
                        jsonData.add(jsonRes);
                    }
                }
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    // service for front end 
    public static String getSalaryCalculationData_ExpenseHeader(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        Map<String, Object> criteria = new HashMap<String, Object>();

        String partyId = request.getParameter("partyId");
        String expenseType = request.getParameter("expenseType");
//        String year = request.getParameter("year");
//        String month = request.getParameter("month");
        String expenseId = request.getParameter("expenseId");

        if (!expenseId.isEmpty()) {
            System.out.println("*" + expenseId + "*");
            criteria.put("expenseId", expenseId);
        }

        criteria.put("partyId", partyId);
        criteria.put("expenseType", expenseType);
        criteria.put("paymentTypeId", "PAY_TYPE_SEPARATED");

        String isCompany = "";
        Map<String, Map<Object, Object>> values = new HashMap<String, Map<Object, Object>>();
        try {
            List<GenericValue> list = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
            System.out.println("***");
            System.out.println(list);
            if (list.size() != 0) {
                for (GenericValue row : list) {
                    isCompany = (String) row.get("isCompany");

                    if (!isCompany.equals("Y")) {
                        String amount = row.get("amount") != null ? row.get("amount").toString() : "0";
                        String expenseIdValue = (String) row.get("expenseId");
                        System.out.println("** expenseIdValue " + expenseIdValue);
                        if (values.isEmpty()) {
                            Map<Object, Object> subValues = new HashMap<Object, Object>();

                            subValues.put("expenseId", expenseIdValue);
                            subValues.put("partyId", row.get("partyId"));
                            subValues.put("fullName", HumanResEvents.getPartyName(request, response, (String) row.get("partyId")));
                            subValues.put("organizationPartyId", (String) row.get("organizationPartyId"));
                            subValues.put("allowenceId", getAllowenceDescription(request, (String) row.get("allowenceId")));
                            subValues.put("amount", row.get("amount"));
                            subValues.put("glAccountId", row.get("glAccountId"));
                            subValues.put("year", row.get("year"));
                            subValues.put("month", row.get("month"));
                            subValues.put("isPosted", row.get("isPosted"));
                            subValues.put("isCompany", row.get("isCompany"));

                            values.put(expenseIdValue, subValues);
                        } else {
                            if (values.containsKey(expenseIdValue)) {
                                Map<Object, Object> subValues = new HashMap<Object, Object>();
                                subValues = values.get(expenseIdValue);

                                BigDecimal newAmount = new BigDecimal(amount);
                                BigDecimal oldAmount = new BigDecimal(values.get(expenseIdValue).get("amount").toString());

                                subValues.put("expenseId", expenseIdValue);
                                subValues.put("partyId", row.get("partyId"));
                                subValues.put("fullName", HumanResEvents.getPartyName(request, response, (String) row.get("partyId")));
                                subValues.put("organizationPartyId", (String) row.get("organizationPartyId"));
                                subValues.put("allowenceId", getAllowenceDescription(request, (String) row.get("allowenceId")));
                                subValues.put("amount", oldAmount.add(newAmount));
                                subValues.put("glAccountId", row.get("glAccountId"));
                                subValues.put("year", row.get("year"));
                                subValues.put("month", row.get("month"));
                                subValues.put("isPosted", row.get("isPosted"));
                                subValues.put("isCompany", row.get("isCompany"));

                                values.put(expenseIdValue, subValues);
                            } else {
                                Map<Object, Object> subValues = new HashMap<Object, Object>();

                                subValues.put("expenseId", expenseIdValue);
                                subValues.put("partyId", row.get("partyId"));
                                subValues.put("fullName", HumanResEvents.getPartyName(request, response, (String) row.get("partyId")));
                                subValues.put("organizationPartyId", (String) row.get("organizationPartyId"));
                                subValues.put("allowenceId", getAllowenceDescription(request, (String) row.get("allowenceId")));
                                subValues.put("amount", row.get("amount"));
                                subValues.put("glAccountId", row.get("glAccountId"));
                                subValues.put("year", row.get("year"));
                                subValues.put("month", row.get("month"));
                                subValues.put("isPosted", row.get("isPosted"));
                                subValues.put("isCompany", row.get("isCompany"));

                                values.put(expenseIdValue, subValues);
                            }
                        }
                    }
                }
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(new JSONObject(values));
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String SalarySlip_Employee(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        Map<String, Object> criteria = new HashMap<String, Object>();

        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        String jobGroup = request.getParameter("jobGroup");
        String partyId = request.getParameter("partyId");
        ;
        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("partyId", partyId);

        try {
            List<GenericValue> list = null;
            if (month == 0) {
                list = delegator.findAll("EmplSalary", true);
            } else {
                list = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
            }
            for (GenericValue row : list) {
                jsonRes = new JSONObject();
                String gradeId = (String) row.get("gradeId");
                jsonRes.put("emplSalaryId", row.get("emplSalaryId"));
                jsonRes.put("partyId", row.get("partyId"));
                jsonRes.put("fullName", HumanResEvents.getPartyName(request, response, (String) row.get("partyId")));
                jsonRes.put("jobGroupId", getJobGroupDescription(request, (String) row.get("jobGroupId")));
                jsonRes.put("organizationPartyId", row.get("organizationPartyId"));
                jsonRes.put("allowenceId", getAllowenceDescription(request,(String) row.get("allowenceId")));
                jsonRes.put("gradeId", getGradeDescription(request,(String) row.get("gradeId")));
                jsonRes.put("degreeId", getDegreeDescription(request, (String) row.get("degreeId")));
                jsonRes.put("amountType", (String) row.get("amountType"));
                String positionTypeId = getEmplPositionTypeId(request, (String) row.get("positionId"));
                jsonRes.put("positionId", getEmplPositionTypeDescription(request, positionTypeId));
                jsonRes.put("amount", row.get("amount"));
                jsonRes.put("glAccountId", row.get("glAccountId"));
                jsonRes.put("year", row.get("year"));
                jsonRes.put("month", row.get("month"));
                jsonRes.put("nomDay", row.get("nomDay"));
                jsonRes.put("isPosted", row.get("isPosted"));
                jsonRes.put("isCompany", row.get("isCompany"));
                jsonData.add(jsonRes);

            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String SalarySlip_AllEmployee(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        double amount_double = 0.0;
        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("year", year);
        criteria.put("month", month);

        Map<String, String> criteriaBasicSalary = new HashMap<String, String>();
        criteriaBasicSalary.put("Key", "BASIC_SALARY_ALLOWENCES_ID");

        Map<String, String> criteriaSocialSec = new HashMap<String, String>();
        criteriaSocialSec.put("Key", "SOCIAL_SECURITY_ALLOWENCES_ID");

        Map<String, String> criteriaTrans = new HashMap<String, String>();
        criteriaTrans.put("Key", "TRANSPORTATION_ALLOWANCE_ID");

        Map<String, String> criteriaEdu = new HashMap<String, String>();
        criteriaEdu.put("Key", "EDUCATION_ALLOWANCE_ID");

        Map<String, String> criteriaTax = new HashMap<String, String>();
        criteriaTax.put("Key", "INCOME_TAX_ALLOWENCES_ID");

        BigDecimal totalBasicSalary = new BigDecimal("0");
        BigDecimal totalTransId = new BigDecimal("0");
        BigDecimal totalEduId = new BigDecimal("0");
        BigDecimal totalSocialSecId = new BigDecimal("0");
        BigDecimal totalTaxId = new BigDecimal("0");
        BigDecimal totalAddition = new BigDecimal("0");
        BigDecimal totalDeduction = new BigDecimal("0");
        BigDecimal totalAmountEmployee = new BigDecimal("0");
        BigDecimal totalGrossBenefits = new BigDecimal("0");

        List<String> listOrder = new ArrayList<>();
        listOrder.add("partyId");

        Map<String, Map<Object, Object>> values = new HashMap<String, Map<Object, Object>>();
        try {
            GenericValue resultBasicSalary = delegator.findOne("GlobalPayrollSettings", criteriaBasicSalary, true);
            String basicSalaryId = (String) resultBasicSalary.get("Value");

            GenericValue resultSocialSec = delegator.findOne("GlobalPayrollSettings", criteriaSocialSec, true);
            String socialSecId = (String) resultSocialSec.get("Value");

            GenericValue resultTrans = delegator.findOne("GlobalPayrollSettings", criteriaTrans, true);
            String transId = (String) resultTrans.get("Value");

            GenericValue resultEdu = delegator.findOne("GlobalPayrollSettings", criteriaEdu, true);
            String eduId = (String) resultEdu.get("Value");

            GenericValue resultTax = delegator.findOne("GlobalPayrollSettings", criteriaTax, true);
            String taxId = (String) resultTax.get("Value");

            List<GenericValue> list = null;
            if (month == 0) {
                System.out.println("error month not found");
            } else {
                list = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, listOrder, null, true);
                System.out.println("** " + list);
            }
            for (GenericValue row : list) {
                String isCompany = row.get("isCompany").toString();
                String gradeId = (String) row.get("gradeId");
                String positionTypeId = getEmplPositionTypeId(request, row.get("positionId").toString());
                String partyId = row.get("partyId").toString();
                String amount = row.get("amount") != null ? row.get("amount").toString() : "0";
                amount_double = (Double) row.get("amount");
                String allowenceId = row.get("allowenceId").toString();
//                String debitCreditFlag = row.get("debitCreditFlag").toString();

                if (values.isEmpty()) {
                    Map<Object, Object> subValues = new HashMap<Object, Object>();
                    BigDecimal newAmount = new BigDecimal(amount);
                    subValues.put("grossBenefits", 0);
                    subValues.put("basicSalary", 0);
                    subValues.put("socialSecId", 0);
                    subValues.put("transId", 0);
                    subValues.put("eduId", 0);
                    subValues.put("taxId", 0);
                    subValues.put("addition", 0);
                    subValues.put("deduction", 0);
                    subValues.put("amountEmployee", 0);
                    subValues.put("amountCompany", 0);
                    subValues.put("emplSalaryId", row.get("emplSalaryId"));
                    subValues.put("partyId", partyId);
                    subValues.put("fullName", HumanResEvents.getPartyName(request, response, partyId));
                    subValues.put("jobGroupId", getJobGroupDescription(request, (String) row.get("jobGroupId")));
                    subValues.put("organizationPartyId", row.get("organizationPartyId"));
                    if (allowenceId.equals(basicSalaryId)) {
                        subValues.put("gradeId", getGradeDescription(request, row.get("gradeId").toString()));
                        subValues.put("degreeId", getDegreeDescription(request, row.get("degreeId").toString()));
                    }
                    subValues.put("positionId", getEmplPositionTypeDescription(request, positionTypeId));
                    subValues.put("nomDay", row.get("nomDay"));
                    subValues.put("isPosted", row.get("isPosted"));
                    if (isCompany.equals("Y")) {
                        subValues.put("amountCompany", amount);
                    } else {
                        if (allowenceId.equals(basicSalaryId)) {
                            subValues.put("basicSalary", amount);
                            totalBasicSalary = totalBasicSalary.add(newAmount);
                        } else if (allowenceId.equals(socialSecId) && isCompany.equals("N")) {
                            subValues.put("socialSecId", amount);
                            totalSocialSecId = totalSocialSecId.add(newAmount);
                        } else if (allowenceId.equals(transId)) {
                            subValues.put("transId", amount);
                            totalTransId = totalTransId.add(newAmount);
                        } else if (allowenceId.equals(eduId)) {
                            subValues.put("eduId", amount);
                            totalEduId = totalEduId.add(newAmount);
                        } else if (allowenceId.equals(taxId)) {
                            subValues.put("taxId", amount);
                            totalTaxId = totalTaxId.add(newAmount);
                        } else if (amount_double > 0) {
                            subValues.put("addition", amount);
                            totalAddition = totalAddition.add(newAmount);
                        } else if (amount_double < 0) {
                            subValues.put("deduction", amount);
                            totalDeduction = totalDeduction.add(newAmount);
                        }
                        if (amount_double > 0) {
                            subValues.put("grossBenefits", amount);
                            totalGrossBenefits = totalGrossBenefits.add(newAmount);
                        }
                        subValues.put("amountEmployee", amount);
                        totalAmountEmployee = totalAmountEmployee.add(newAmount);
                    }
                    values.put(partyId, subValues);
                } else {
                    if (values.containsKey(partyId)) {
                        Map<Object, Object> subValues = new HashMap<Object, Object>();
                        subValues = values.get(partyId);
                        BigDecimal oldGrossBenefits = new BigDecimal(values.get(partyId).get("grossBenefits").toString());
                        if (allowenceId.equals(basicSalaryId)) {
                            subValues.put("gradeId", getGradeDescription(request, row.get("gradeId").toString()));
                            subValues.put("degreeId", getDegreeDescription(request, row.get("degreeId").toString()));
                        }
                        if (isCompany.equals("Y")) {
                            BigDecimal newAmount = new BigDecimal(amount);
                            BigDecimal oldAmount = new BigDecimal(values.get(partyId).get("amountCompany").toString());
                            subValues.put("amountCompany", oldAmount.add(newAmount));
                        } else {
                            BigDecimal newAmount = new BigDecimal(amount);
                            BigDecimal oldAmount = new BigDecimal(values.get(partyId).get("amountEmployee").toString());
//                            subValues.put("amountEmployee", oldAmount.add(newAmount));
                            if (allowenceId.equals(basicSalaryId)) {
                                BigDecimal oldBasic = new BigDecimal(values.get(partyId).get("basicSalary").toString());
                                subValues.put("basicSalary", oldBasic.add(newAmount));
                                totalBasicSalary = totalBasicSalary.add(newAmount);
                            } else if (allowenceId.equals(socialSecId) && isCompany.equals("N")) {
                                BigDecimal oldSocialSecId = new BigDecimal(values.get(partyId).get("socialSecId").toString());
                                subValues.put("socialSecId", oldSocialSecId.add(newAmount));
                                totalSocialSecId = totalSocialSecId.add(newAmount);
                            } else if (allowenceId.equals(transId)) {
                                BigDecimal oldTransId = new BigDecimal(values.get(partyId).get("transId").toString());
                                subValues.put("transId", oldTransId.add(newAmount));
                                totalTransId = totalTransId.add(newAmount);
                            } else if (allowenceId.equals(eduId)) {
                                BigDecimal oldEduId = new BigDecimal(values.get(partyId).get("eduId").toString());
                                subValues.put("eduId", oldEduId.add(newAmount));
                                totalEduId = totalEduId.add(newAmount);
                            } else if (allowenceId.equals(taxId)) {
                                BigDecimal oldtaxId = new BigDecimal(values.get(partyId).get("taxId").toString());
                                subValues.put("taxId", oldtaxId.add(newAmount));
                                totalTaxId = totalTaxId.add(newAmount);
                            } else if (amount_double > 0) {
                                BigDecimal oldAddition = new BigDecimal(values.get(partyId).get("addition").toString());
                                subValues.put("addition", oldAddition.add(newAmount));
                                totalAddition = totalAddition.add(newAmount);
                            } else if (amount_double < 0) {
                                BigDecimal oldDeduction = new BigDecimal(values.get(partyId).get("deduction").toString());
                                subValues.put("deduction", oldDeduction.add(newAmount));
                                totalDeduction = totalDeduction.add(newAmount);
                            }
                            if (amount_double > 0) {
                                subValues.put("grossBenefits", oldGrossBenefits.add(newAmount));
                                totalGrossBenefits = totalGrossBenefits.add(newAmount);
                            }
                            subValues.put("amountEmployee", oldAmount.add(newAmount));
                            totalAmountEmployee = totalAmountEmployee.add(newAmount);
                        }
                        values.put(partyId, subValues);
                    } else {
                        Map<Object, Object> subValues = new HashMap<Object, Object>();
                        BigDecimal newAmount = new BigDecimal(amount);
                        subValues.put("grossBenefits", 0);
                        subValues.put("basicSalary", 0);
                        subValues.put("socialSecId", 0);
                        subValues.put("transId", 0);
                        subValues.put("eduId", 0);
                        subValues.put("taxId", 0);
                        subValues.put("addition", 0);
                        subValues.put("deduction", 0);
                        subValues.put("amountEmployee", 0);
                        subValues.put("amountCompany", 0);
                        subValues.put("emplSalaryId", row.get("emplSalaryId"));
                        subValues.put("partyId", partyId);
                        subValues.put("fullName", HumanResEvents.getPartyName(request, response, partyId));
                        subValues.put("jobGroupId", getJobGroupDescription(request, (String) row.get("jobGroupId")));
                        subValues.put("organizationPartyId", row.get("organizationPartyId"));
                        if (allowenceId.equals(basicSalaryId)) {
                            subValues.put("gradeId", getGradeDescription(request, row.get("gradeId").toString()));
                            subValues.put("degreeId", getDegreeDescription(request, row.get("degreeId").toString()));
                        }
                        subValues.put("positionId", getEmplPositionTypeDescription(request, positionTypeId));
                        subValues.put("nomDay", row.get("nomDay"));
                        subValues.put("isPosted", row.get("isPosted"));
                        if (isCompany.equals("Y")) {
                            subValues.put("amountCompany", amount);
                        } else {
                            if (allowenceId.equals(basicSalaryId)) {
                                subValues.put("basicSalary", amount);
                                totalBasicSalary = totalBasicSalary.add(newAmount);
                            } else if (allowenceId.equals(socialSecId) && isCompany.equals("N")) {
                                subValues.put("socialSecId", amount);
                                totalSocialSecId = totalSocialSecId.add(newAmount);
                            } else if (allowenceId.equals(transId)) {
                                subValues.put("transId", amount);
                                totalTransId = totalTransId.add(newAmount);
                            } else if (allowenceId.equals(eduId)) {
                                subValues.put("eduId", amount);
                                totalEduId = totalEduId.add(newAmount);
                            } else if (allowenceId.equals(taxId)) {
                                subValues.put("taxId", amount);
                                totalTaxId = totalTaxId.add(newAmount);
                            } else if (amount_double > 0) {
                                subValues.put("addition", amount);
                                totalAddition = totalAddition.add(newAmount);
                            } else if (amount_double < 0) {
                                subValues.put("deduction", amount);
                                totalDeduction = totalDeduction.add(newAmount);
                            }
                            if (amount_double > 0) {
                                subValues.put("grossBenefits", amount);
                                totalGrossBenefits = totalGrossBenefits.add(newAmount);
                            }
                            subValues.put("amountEmployee", amount);
                            totalAmountEmployee = totalAmountEmployee.add(newAmount);
                        }
                        values.put(partyId, subValues);
                    }
                }
            }

            Map<Object, Object> totals = new HashMap<Object, Object>();
                totals.put("totalGrossBenefits", totalGrossBenefits);
            totals.put("totalBasicSalary", totalBasicSalary);
            totals.put("totalSocialSecId", totalSocialSecId);
            totals.put("totalTransId", totalTransId);
            totals.put("totalEduId", totalEduId);
            totals.put("totalTaxId", totalTaxId);
            totals.put("totalAddition", totalAddition);
            totals.put("totalDeduction", totalDeduction);
            totals.put("totalAmountEmployee", totalAmountEmployee);
            values.put("totals", totals);

            Map<String, Map<Object, Object>> treeMap = new TreeMap<String, Map<Object, Object>>(values);
            System.out.println("** treeMap **");
            System.out.println(treeMap);

            ObjRecordSalarySlipAll record = new ObjRecordSalarySlipAll();
            listSalarySlipAll.clear();
            for (Map.Entry<String, Map<Object, Object>> entry : treeMap.entrySet()) {
                if (!entry.getKey().equals("totals")) {
                    String basicSalary = entry.getValue().get("basicSalary") != null ? entry.getValue().get("basicSalary").toString() : "0";
                    String addition = entry.getValue().get("addition") != null ? entry.getValue().get("addition").toString() : "0";
                    String deduction = entry.getValue().get("deduction") != null ? entry.getValue().get("deduction").toString() : "0";
                    String socialSecIdSub = entry.getValue().get("socialSecId") != null ? entry.getValue().get("socialSecId").toString() : "0";
                    String transIdSub = entry.getValue().get("transId") != null ? entry.getValue().get("transId").toString() : "0";
                    String eduIdSub = entry.getValue().get("eduId") != null ? entry.getValue().get("eduId").toString() : "0";
                    String taxIdSub = entry.getValue().get("taxId") != null ? entry.getValue().get("taxId").toString() : "0";
                    String grossBenefitsSub = entry.getValue().get("grossBenefits") != null ? entry.getValue().get("grossBenefits").toString() : "0";

                    record = new ObjRecordSalarySlipAll();
                    record.setPartyId(entry.getValue().get("partyId") + "");
                    record.setFullName(entry.getValue().get("fullName") + "");
                    record.setOrganizationPartyId(entry.getValue().get("organizationPartyId") + "");
                    record.setGradeId(entry.getValue().get("gradeId") + "");
                    record.setDegreeId(entry.getValue().get("degreeId") + "");
                    record.setAmountEmployee(entry.getValue().get("amountEmployee") + "");
                    record.setAmountCompany(entry.getValue().get("amountCompany") + "");
                    record.setIsPosted(entry.getValue().get("isPosted") + "");
                    record.setBasicSalary(basicSalary);
                    record.setAddition(addition);
                    record.setDeduction(deduction);
                    record.setSocialSecId(socialSecIdSub);
                    record.setTransId(transIdSub);
                    record.setEduId(eduIdSub);
                    record.setTaxId(taxIdSub);
                    record.setGrossBenefits(grossBenefitsSub);
                    listSalarySlipAll.add(record);
                }
            }

//            List<ObjRecordSalarySlipAll> listV = new Vector();
//            listV = listSalarySlipAll;
//            Collections.sort(listV);
            System.out.println("** SalarySlip_AllEmployee **");
            System.out.println(treeMap);
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
//            out.println(values);
            out.println(new JSONObject(values));
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String SalarySlip_AllEmployeePdf(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        System.out.println("** SalarySlip_AllEmployeePdf **");
        System.out.println(listSalarySlipAll);
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String FOLANG = (String) request.getSession().getAttribute("FOLANG");

        String year = request.getParameter("year") + "";
        String month = request.getParameter("month") + "";
        System.out.println("year " + year + " month" + month);

        Map<String, String> monthAr = new HashMap<String, String>();
        monthAr.put("January", "كانون الثاني");
        monthAr.put("February", "شباط");
        monthAr.put("March", "آذار");
        monthAr.put("April", "نيسان");
        monthAr.put("May", "أيار");
        monthAr.put("June", "حزيران");
        monthAr.put("July", "تموز");
        monthAr.put("August", "آب");
        monthAr.put("September", "أيلول");
        monthAr.put("October", "تشرين الأول");
        monthAr.put("November", "تشرين الثاني");
        monthAr.put("December", "كانون الأول");

        ResultSet resultSet = null;
        GenericValue userLogin = null;
        response.setContentType("application/pdf");
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("partyId", "Company");
        try {
            GenericValue resultPartyGroup = delegator.findOne("PartyGroup", criteria, true);
            String groupName = (String) resultPartyGroup.get("groupName");

            String jrxmlFile = "applications\\reports\\payroll\\SalarySlipAll.jrxml";
            if (FOLANG.equals("en")) {
                jrxmlFile = "applications\\reports\\payroll\\SalarySlipAll.jrxml";
                groupName = (String) resultPartyGroup.get("groupName");
            } else if (FOLANG.equals("ar")) {
                jrxmlFile = "applications\\reports\\payroll\\SalarySlipAll_AR.jrxml";
                groupName = (String) resultPartyGroup.get("groupNameLocal");
            }

            InputStream input = new FileInputStream(new File(jrxmlFile));
            JasperReport jasperReport = JasperCompileManager.compileReport(input);

            HashMap[] reportRows = null;
            reportRows = new HashMap[listSalarySlipAll.size()];
            HashMap row = new HashMap();
//            for (int i = 0; i < listSalarySlipAll.size(); i++) {

//                row = new HashMap();
//                row.put("PartyId", listSalarySlipAll.get(i).getPartyId());
//                row.put("GroupName", listSalarySlipAll.get(i).getGroupName());
//                row.put("CR", listSalarySlipAll.get(i).getCR());
//                row.put("DR", listSalarySlipAll.get(i).getDR());
//                row.put("Balance", listSalarySlipAll.get(i).getBalance());
//                reportRows[i] = row;
//            }
            try {
                userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
                String printed_By = userLogin.getString("partyId");
                String userLoginId = userLogin.getString("userLoginId");

                Map<String, Object> params = new HashMap<String, Object>();
//                params.put("company", request.getParameter("company") + " - " + request.getParameter("groupName"));
                params.put("groupName", groupName);
                params.put("userLoginId", userLoginId);
                params.put("imageUrl", PayrollReports.getLogoUrl(request));
                params.put("year", year);
                params.put("month", month);
                params.put("totalCompany", totalCompanyGlobal);
                params.put("totalEmployee", totalEmployeeGlobal);

//                JRBeanArrayDataSource ds = new JRBeanArrayDataSource(listSalarySlipAll);
                JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listSalarySlipAll);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);

                JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        return "";
    }

    public static String getAllGlAccountDropDown(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "<select>";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("GlAccountOrganizationAndClass", true);
//            PrintWriter pw = new PrintWriter(response.getOutputStream());
            for (GenericValue genericValue : gv1) {

                str += "<option value='" + genericValue.get("glAccountId") + "'>" + genericValue.get("accountCode") + " - " + genericValue.get("accountName") + " [" + genericValue.get("glAccountId") + "] " + "</option>";
            }
            str += "</select>";
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getAllGlAccountDropDown_settingScreen(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String str = "";
        PrintWriter out = null;
        try {
            List<GenericValue> gv1 = delegator.findAll("GlAccountOrganizationAndClass", true);
//            PrintWriter pw = new PrintWriter(response.getOutputStream());
            for (GenericValue genericValue : gv1) {

                str += "<option value='" + genericValue.get("glAccountId") + "'>" + genericValue.get("accountCode") + " - " + genericValue.get("accountName") + " [" + genericValue.get("glAccountId") + "] " + "</option>";
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getEmplSalaryControlData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        JSONArray jsonData = new JSONArray();

        JSONObject jsonRes = new JSONObject();
        try {

            List<GenericValue> list = null;
            if (year == 0) {
                list = delegator.findAll("EmplSalaryControl", true);
            } else if (month == 0) {
                criteria.put("year", year);
                list = delegator.findList("EmplSalaryControl", EntityCondition.makeCondition(criteria), null, null, null, true);

            } else {
                criteria.put("year", year);
                criteria.put("month", month);
                list = delegator.findList("EmplSalaryControl", EntityCondition.makeCondition(criteria), null, null, null, true);
            }

            for (GenericValue row : list) {
                jsonRes = new JSONObject();
                jsonRes.put("EmplSalaryControlId", row.get("EmplSalaryControlId"));
                jsonRes.put("year", row.get("year"));
                jsonRes.put("month", row.get("month"));
//                jsonRes.put("amount", row.get("amount"));
                if (row.get("transDate") == null) {
                    jsonRes.put("transDate", null);
                } else {
                    jsonRes.put("transDate", row.get("transDate").toString());
                }

                jsonRes.put("totalFlaq", row.get("totalFlaq"));
                jsonRes.put("userLoginId", row.get("userLoginId"));
                jsonData.add(jsonRes);
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getEmplSalaryPartailyData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        String jobGroup = request.getParameter("jobGroup");
        String dept = request.getParameter("dept");
        String partyId = request.getParameter("partyId");

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("jobGroupId", jobGroup);

        if (!dept.isEmpty()) {
            criteria.put("organizationPartyId", dept);
        }
        if (!partyId.isEmpty()) {

            criteria.put("partyId", partyId);
        }

        Set<String> allowences_Distinct = new HashSet<String>();

        JSONArray jsonData = new JSONArray();
        JSONObject jsonRes = new JSONObject();
        try {
            List<String> listOrder = new ArrayList<>();
            listOrder.add("emplPartailyId");
            List<GenericValue> list = delegator.findList("EmplSalaryPartialy", EntityCondition.makeCondition(criteria), null, listOrder, null, true);
            for (GenericValue row : list) {
                jsonRes = new JSONObject();
                jsonRes.put("emplPartailyId", row.get("emplPartailyId"));
                jsonRes.put("partyId", row.get("partyId"));
                jsonRes.put("fullName", HumanResEvents.getPartyName(request, response, (String) row.get("partyId")));
                jsonRes.put("allowenceId", getAllowenceDescription(request, row.get("allowenceId").toString()));
                jsonRes.put("jobGroupId", getJobGroupDescription(request, (String) row.get("jobGroupId")));
                jsonRes.put("organizationPartyId", row.get("organizationPartyId"));
                jsonRes.put("year", row.get("year"));
                jsonRes.put("month", row.get("month"));
                jsonRes.put("amount", row.get("amount"));
                jsonRes.put("calculationType", row.get("calculationType"));
                jsonRes.put("calculationAmount", row.get("calculationAmount"));
                jsonRes.put("newAmount", row.get("newAmount"));
                jsonRes.put("remainingAmount", row.get("remainingAmount"));
                jsonRes.put("isPosted", row.get("isPosted"));
                jsonRes.put("seq", row.get("seq"));
                jsonRes.put("userLoginId", row.get("userLoginId"));
                jsonRes.put("isCompany", row.get("isCompany"));
                jsonRes.put("glAccountId", row.get("glAccountId"));
                allowences_Distinct.add(row.get("allowenceId").toString());
                jsonData.add(jsonRes);
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getEmplSalaryPartailyData_ByMonth(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        String jobGroup = request.getParameter("jobGroup");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("jobGroupId", jobGroup);
        criteria.put("isPosted", "N");
        JSONArray jsonData = new JSONArray();
        JSONObject jsonRes = new JSONObject();
        try {
            List<String> listOrder = new ArrayList<>();
            listOrder.add("emplPartailyId");
            List<GenericValue> list = delegator.findList("EmplSalEmplSalPartialyView", EntityCondition.makeCondition(criteria), null, listOrder, null, true);
            for (GenericValue row : list) {
                jsonRes = new JSONObject();
                jsonRes.put("emplPartailyId", row.get("emplPartailyId"));
                jsonRes.put("emplSalaryId", row.get("emplSalaryId"));
                jsonRes.put("partyId", row.get("partyId"));
                jsonRes.put("fullName", HumanResEvents.getPartyName(request, response, (String) row.get("partyId")));
                jsonRes.put("allowenceId", getAllowenceDescription(request, row.get("allowenceId").toString()));

                jsonRes.put("year", row.get("year"));
                jsonRes.put("month", row.get("month"));
                jsonRes.put("amount", row.get("amount"));
                jsonRes.put("calculationType", row.get("calculationType"));
                jsonRes.put("calculationAmount", row.get("calculationAmount"));
                jsonRes.put("newAmount", row.get("newAmount"));
                jsonRes.put("remainingAmount", row.get("remainingAmount"));
                jsonRes.put("isPosted", row.get("isPosted"));
                jsonRes.put("userLoginId", row.get("userLoginId"));
                jsonData.add(jsonRes);
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getSalaryCalculationData_PartialyScreen(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();

        Map<String, Object> criteria = new HashMap<String, Object>();
        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        String jobGroup = request.getParameter("jobGroup");
        String partyId = request.getParameter("partyId");

        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("jobGroupId", jobGroup);
        criteria.put("partyId", partyId);

        try {
            List<GenericValue> list = null;

            list = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : list) {
                jsonRes = new JSONObject();
                jsonRes.put("emplSalaryId", row.get("emplSalaryId"));
                jsonRes.put("partyId", row.get("partyId"));
                jsonRes.put("fullName", HumanResEvents.getPartyName(request, response, (String) row.get("partyId")));
                jsonRes.put("jobGroupId", getJobGroupDescription(request, (String) row.get("jobGroupId")));
                jsonRes.put("organizationPartyId", row.get("organizationPartyId"));
                jsonRes.put("allowenceId", getAllowenceDescription(request, row.get("allowenceId").toString()));
                jsonRes.put("gradeId", getGradeDescription(request, row.get("gradeId").toString()));
                jsonRes.put("degreeId", getDegreeDescription(request, row.get("degreeId").toString()));
                jsonRes.put("amount", row.get("amount"));
                jsonRes.put("year", row.get("year"));
                jsonRes.put("month", row.get("month"));
                jsonRes.put("nomDay", row.get("nomDay"));

                jsonData.add(jsonRes);
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    // for partialy
    public static String isPosted(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();

        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        Long seq = Long.parseLong(request.getParameter("seq"), 10);
        String partyId = request.getParameter("partyId");

        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("isPosted", "N");
        if (seq == 0.0) {

        } else {
            criteria.put("seq", seq);
        }

        if (!partyId.isEmpty()) {
            criteria.put("partyId", partyId);
        }
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        try {
            jsonRes = new JSONObject();
            List<GenericValue> result = delegator.findList("EmplSalaryPartialy", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (result.isEmpty()) {
                jsonRes.put("result", "true");
            } else if (result.get(0).get("isPosted").toString().equals("Y")) {
                jsonRes.put("result", "true");
            } else {
                jsonRes.put("result", "false");
            }
            jsonData.add(jsonRes);
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String monthIsExist(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        criteria.put("year", year);
        criteria.put("month", month);

        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        try {
            jsonRes = new JSONObject();
            List<GenericValue> result = delegator.findList("EmplSalaryPartialy", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (result.size() >= 1) {
                jsonRes.put("result", "true");
            } else {
                jsonRes.put("result", "false");
            }
            jsonData.add(jsonRes);
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String fillPartialy(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException, SQLException {
        GenericValue userLogin = null;
        userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        String userLoginId = (String) userLogin.getString("userLoginId");
        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        String jobGroup = request.getParameter("jobGroup");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Long seq = 1L;
        Long orig_seq = 0L;
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("jobGroupId", jobGroup);
        try {
            List seqList = new ArrayList();
            List<GenericValue> listSeqPartialy = delegator.findList("EmplSalaryPartialy", EntityCondition.makeCondition(criteria), null, null, null, true);

            if (listSeqPartialy.size() != 0) {
                for (GenericValue row : listSeqPartialy) {
                    seqList.add(row.get("seq"));
                }
                seq = (Long) Collections.max(seqList) + 1;
                orig_seq = (Long) Collections.max(seqList);
            }

            List<String> listOrder = new ArrayList<>();
            listOrder.add("emplSalaryId");
            List<GenericValue> list = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, listOrder, null, true);
            GenericValue itemPartialy = delegator.makeValue("EmplSalaryPartialy");
            for (GenericValue row : list) {
//                if (!row.get("isCompany").toString().equals("Y")) {
                itemPartialy.put("emplPartailyId", delegator.getNextSeqId("emplPartailyId"));
                itemPartialy.put("partyId", row.get("partyId"));
                itemPartialy.put("jobGroupId", row.get("jobGroupId"));
                itemPartialy.put("organizationPartyId", row.get("organizationPartyId"));
                itemPartialy.put("allowenceId", row.get("allowenceId"));
                itemPartialy.put("gradeId", row.get("gradeId"));
                itemPartialy.put("degreeId", row.get("degreeId"));
                itemPartialy.put("amount", row.get("amount"));
                itemPartialy.put("year", row.get("year"));
                itemPartialy.put("month", row.get("month"));
                itemPartialy.put("seq", seq);
                itemPartialy.put("nomDay", row.get("nomDay"));
                itemPartialy.put("calculationType", "Percentage");
                itemPartialy.put("calculationAmount", 0.0);
                itemPartialy.put("newAmount", 0.0);
                itemPartialy.put("glAccountId", row.get("glAccountId"));
                itemPartialy.put("isCompany", row.get("isCompany"));

                List<GenericValue> EmplSalaryPartialy_list = delegator.findList("EmplSalaryPartialy", EntityCondition.makeCondition(criteria), null, null, null, true);

                if (listSeqPartialy.isEmpty()) {
                    itemPartialy.put("remainingAmount", row.get("amount"));
                } else {

                    //
                    double PrevRemainingAmount = 0.0;
                    String PartyID = "";

                    for (int i = 0; i < EmplSalaryPartialy_list.size(); i++) {

                        Map<String, Object> partyId_criteria = new HashMap<String, Object>();

                        partyId_criteria.put("partyId", row.get("partyId"));
                        partyId_criteria.put("allowenceId", row.get("allowenceId"));
                        Long seq_partyId_criteria = (Long) EmplSalaryPartialy_list.get(i).get("seq");

                        partyId_criteria.put("seq", (orig_seq));

                        List<String> remaininglistOrder = new ArrayList<>();
                        listOrder.add("emplPartailyId");

                        List<GenericValue> partyId_list = delegator.findList("EmplSalaryPartialy", EntityCondition.makeCondition(partyId_criteria), null, remaininglistOrder, null, true);

                        for (GenericValue row_partyId_list : partyId_list) {
                            PrevRemainingAmount = Double.parseDouble(row_partyId_list.get("remainingAmount").toString());

                        }

                    }
                    itemPartialy.put("remainingAmount", PrevRemainingAmount);
                }
                itemPartialy.put("isPosted", "N");
                itemPartialy.put("userLoginId", userLoginId);
                itemPartialy.create();
//                }

            }
        } catch (Exception ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
            return "error";
        }
        return "success";
    }

    public static String setDeptPartailyAllow(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        GenericValue userLogin = null;
        userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        String userLoginId = (String) userLogin.getString("userLoginId");
        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        String jobGroup = request.getParameter("jobGroup");
        String dept = request.getParameter("dept");

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("jobGroupId", jobGroup);

        Map<String, Object> deptPartailyAllowCriteria = new HashMap<String, Object>();
        deptPartailyAllowCriteria.put("year", year);
        deptPartailyAllowCriteria.put("month", month);
        deptPartailyAllowCriteria.put("jobGroupId", jobGroup);

        Map<String, Object> removeCriteria = new HashMap<String, Object>();
        removeCriteria.put("year", year);
        removeCriteria.put("month", month);
        removeCriteria.put("jobGroupId", jobGroup);
        removeCriteria.put("percentageVal", 0.0);

        if (!dept.isEmpty()) {
            criteria.put("organizationPartyId", dept);
            removeCriteria.put("organizationPartyId", dept);
            deptPartailyAllowCriteria.put("organizationPartyId", dept);
        }

        Set<String> allowences_Distinct = new HashSet<String>();
        Long seq = 0L;
        JSONArray jsonData = new JSONArray();
        JSONObject jsonRes = new JSONObject();
        GenericValue itemPartialy = delegator.makeValue("deptPartailyAllow");
        try {
            List<String> listOrder = new ArrayList<>();
            listOrder.add("seq");
            List<GenericValue> list = delegator.findList("EmplSalaryPartialy", EntityCondition.makeCondition(criteria), null, listOrder, null, true);
            for (GenericValue row : list) {
                seq = (Long) row.get("seq");
            }
            removeCriteria.put("seq", seq);
            deptPartailyAllowCriteria.put("seq", seq);
            int remove = delegator.removeByAnd("deptPartailyAllow", removeCriteria);
            Set<String> fieldsToSelect = new HashSet<String>();
            fieldsToSelect.add("allowenceId");
            List<GenericValue> list_deptPartailyAllow = delegator.findList("deptPartailyAllow", EntityCondition.makeCondition(deptPartailyAllowCriteria), fieldsToSelect, null, null, true);

            List<String> list_allowenceId = new ArrayList<>();
            for (GenericValue row : list_deptPartailyAllow) {
                list_allowenceId.add(row.get("allowenceId").toString());
            }

            for (GenericValue row : list) {
                if (!list_allowenceId.contains(row.get("allowenceId"))) {
                    allowences_Distinct.add(row.get("allowenceId").toString());
                }
//                seq = (Long) row.get("seq");
            }

            for (String item : allowences_Distinct) {
                itemPartialy.put("deptPartailyAllowId", delegator.getNextSeqId("deptPartailyAllowId"));
                itemPartialy.put("allowenceId", item);
                itemPartialy.put("organizationPartyId", dept);
                itemPartialy.put("jobGroupId", jobGroup);
                itemPartialy.put("year", year);
                itemPartialy.put("month", month);
                itemPartialy.put("percentageVal", 0.0);
                itemPartialy.put("seq", seq);
                itemPartialy.put("userLoginId", userLoginId);
                itemPartialy.create();

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String setAllPartailyAllow(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        GenericValue userLogin = null;
        userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        String userLoginId = (String) userLogin.getString("userLoginId");
        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        String jobGroup = request.getParameter("jobGroup");
//        String dept = request.getParameter("dept");

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("jobGroupId", jobGroup);

        Map<String, Object> deptPartailyAllowCriteria = new HashMap<String, Object>();
        deptPartailyAllowCriteria.put("year", year);
        deptPartailyAllowCriteria.put("month", month);
        deptPartailyAllowCriteria.put("jobGroupId", jobGroup);

        Map<String, Object> removeCriteria = new HashMap<String, Object>();
        removeCriteria.put("year", year);
        removeCriteria.put("month", month);
        removeCriteria.put("jobGroupId", jobGroup);
        removeCriteria.put("percentageVal", 0.0);

//        if (!dept.isEmpty()) {
//            criteria.put("organizationPartyId", dept);
//            removeCriteria.put("organizationPartyId", dept);
//            deptPartailyAllowCriteria.put("organizationPartyId", dept);
//        }
        Set<String> allowences_Distinct = new HashSet<String>();
        Long seq = 0L;
        JSONArray jsonData = new JSONArray();
        JSONObject jsonRes = new JSONObject();
        GenericValue itemPartialy = delegator.makeValue("allPartailyAllow");
        try {
            List<String> listOrder = new ArrayList<>();
            listOrder.add("seq");
            List<GenericValue> list = delegator.findList("EmplSalaryPartialy", EntityCondition.makeCondition(criteria), null, listOrder, null, true);
            for (GenericValue row : list) {
                seq = (Long) row.get("seq");
            }
            removeCriteria.put("seq", seq);
            deptPartailyAllowCriteria.put("seq", seq);
            int remove = delegator.removeByAnd("allPartailyAllow", removeCriteria);
            Set<String> fieldsToSelect = new HashSet<String>();
            fieldsToSelect.add("allowenceId");
            List<GenericValue> list_deptPartailyAllow = delegator.findList("allPartailyAllow", EntityCondition.makeCondition(deptPartailyAllowCriteria), fieldsToSelect, null, null, true);

            List<String> list_allowenceId = new ArrayList<>();
            for (GenericValue row : list_deptPartailyAllow) {
                list_allowenceId.add(row.get("allowenceId").toString());
            }

            for (GenericValue row : list) {
                if (!list_allowenceId.contains(row.get("allowenceId"))) {
                    allowences_Distinct.add(row.get("allowenceId").toString());
                }
//                seq = (Long) row.get("seq");
            }

            for (String item : allowences_Distinct) {
                itemPartialy.put("allPartailyAllowId", delegator.getNextSeqId("allPartailyAllowId"));
                itemPartialy.put("allowenceId", item);
//                itemPartialy.put("organizationPartyId", dept);
                itemPartialy.put("jobGroupId", jobGroup);
                itemPartialy.put("year", year);
                itemPartialy.put("month", month);
                itemPartialy.put("percentageVal", 0.0);
                itemPartialy.put("seq", seq);
                itemPartialy.put("userLoginId", userLoginId);
                itemPartialy.create();

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getDeptPartailyAllowData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        String jobGroup = request.getParameter("jobGroup");
        String dept = request.getParameter("dept");

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("jobGroupId", jobGroup);
        criteria.put("organizationPartyId", dept);

        JSONArray jsonData = new JSONArray();
        JSONObject jsonRes = new JSONObject();
        try {

            List<GenericValue> list = delegator.findList("deptPartailyAllow", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : list) {
                jsonRes = new JSONObject();
                jsonRes.put("deptPartailyAllowId", row.get("deptPartailyAllowId"));

                jsonRes.put("jobGroupId", row.get("jobGroupId"));
                jsonRes.put("jobGroupId_Desc", getJobGroupDescription(request, (String) row.get("jobGroupId")));

                jsonRes.put("organizationPartyId", row.get("organizationPartyId"));

                jsonRes.put("allowenceId", row.get("allowenceId"));
                jsonRes.put("allowenceId_Desc", getAllowenceDescription(request, row.get("allowenceId").toString()));

                jsonRes.put("year", row.get("year"));
                jsonRes.put("month", row.get("month"));
                jsonRes.put("percentageVal", row.get("percentageVal"));
                jsonRes.put("seq", row.get("seq"));
                jsonRes.put("userLoginId", row.get("userLoginId"));

                jsonData.add(jsonRes);
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getAllPartailyAllowData(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        String jobGroup = request.getParameter("jobGroup");
//        String dept = request.getParameter("dept");

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("jobGroupId", jobGroup);
//        criteria.put("organizationPartyId", dept);

        JSONArray jsonData = new JSONArray();
        JSONObject jsonRes = new JSONObject();
        try {

            List<GenericValue> list = delegator.findList("allPartailyAllow", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (GenericValue row : list) {
                jsonRes = new JSONObject();
                jsonRes.put("allPartailyAllowId", row.get("allPartailyAllowId"));

                jsonRes.put("jobGroupId", row.get("jobGroupId"));
                jsonRes.put("jobGroupId_Desc", getJobGroupDescription(request, (String) row.get("jobGroupId")));

                jsonRes.put("organizationPartyId", row.get("organizationPartyId"));

                jsonRes.put("allowenceId", row.get("allowenceId"));
                jsonRes.put("allowenceId_Desc", getAllowenceDescription(request, row.get("allowenceId").toString()));

                jsonRes.put("year", row.get("year"));
                jsonRes.put("month", row.get("month"));
                jsonRes.put("percentageVal", row.get("percentageVal"));
                jsonRes.put("seq", row.get("seq"));
                jsonRes.put("userLoginId", row.get("userLoginId"));

                jsonData.add(jsonRes);
            }

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String updateDeptSalaryPartaily(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        String deptPartailyAllowId = request.getParameter("deptPartailyAllowId");
        double percentageVal = Double.parseDouble(request.getParameter("percentageVal"));
//        Long seq = Long.parseLong(request.getParameter("seq"));

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("deptPartailyAllowId", deptPartailyAllowId);
        boolean beganTransaction = false;
        try {
            beganTransaction = TransactionUtil.begin();

            List<GenericValue> list = delegator.findList("deptPartailyAllow", EntityCondition.makeCondition(criteria), null, null, null, true);

            GenericValue gvValue = (GenericValue) list.get(0).clone();
            gvValue.set("percentageVal", percentageVal);
            gvValue.store();

            updateSalaryPartaily(request);
//            updateEmplSalaryPartaily(request, response);

        } catch (GenericEntityException ex) {
            try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
                return "error";
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                return "error";
            }

        } finally {
            // only commit the transaction if we started one... this will throw an exception if it fails
            try {
                TransactionUtil.commit(beganTransaction);

            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not commit transaction for entity engine error occurred while saving abandoned cart information", module);
                return "error";
            }
        }
        return "";
    }

    public static String updateAllSalaryPartaily(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        String allPartailyAllowId = request.getParameter("allPartailyAllowId");
        double percentageVal = Double.parseDouble(request.getParameter("percentageVal"));
//        Long seq = Long.parseLong(request.getParameter("seq"));

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("allPartailyAllowId", allPartailyAllowId);
        boolean beganTransaction = false;
        try {
            beganTransaction = TransactionUtil.begin();

            List<GenericValue> list = delegator.findList("allPartailyAllow", EntityCondition.makeCondition(criteria), null, null, null, true);

            GenericValue gvValue = (GenericValue) list.get(0).clone();
            gvValue.set("percentageVal", percentageVal);
            gvValue.store();

            updateSalaryPartaily(request);
//            updateEmplSalaryPartaily(request, response);

        } catch (GenericEntityException ex) {
            try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
                return "error";
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                return "error";
            }

        } finally {
            // only commit the transaction if we started one... this will throw an exception if it fails
            try {
                TransactionUtil.commit(beganTransaction);

            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not commit transaction for entity engine error occurred while saving abandoned cart information", module);
                return "error";
            }
        }
        return "";
    }

    public static String updateSalaryPartaily(HttpServletRequest request) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        boolean beganTransaction = false;

        double percentageVal = Double.parseDouble(request.getParameter("percentageVal"));
        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        String jobGroupId = request.getParameter("jobGroupId");
        String organizationPartyId = request.getParameter("organizationPartyId");
        String allowenceId = request.getParameter("allowenceId");
        Long seq = Long.parseLong(request.getParameter("seq"));

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("jobGroupId", jobGroupId);
        criteria.put("seq", seq);
        criteria.put("allowenceId", allowenceId);

        if (!organizationPartyId.isEmpty()) {
            criteria.put("organizationPartyId", organizationPartyId);
        }

        try {
            beganTransaction = TransactionUtil.begin();

            List<GenericValue> list = delegator.findList("EmplSalaryPartialy", EntityCondition.makeCondition(criteria), null, null, null, true);
            for (int i = 0; i < list.size(); i++) {
                GenericValue gvValue = (GenericValue) list.get(i).clone();
                gvValue.set("calculationType", "Percentage");

                //TO Get Previous remaining Amount to Validation
                Map<String, Object> partyId_criteria = new HashMap<String, Object>();
                partyId_criteria.put("partyId", list.get(i).get("partyId"));
                partyId_criteria.put("allowenceId", list.get(i).get("allowenceId"));
                Long seq_partyId_criteria = (Long) list.get(i).get("seq");
//                partyId_criteria.put("seq", (seq_partyId_criteria - 1));

                List<String> listOrder = new ArrayList<>();
                listOrder.add("emplPartailyId");

                // List to get Previous Values
                List<GenericValue> partyId_list = delegator.findList("EmplSalaryPartialy", EntityCondition.makeCondition(partyId_criteria), null, listOrder, null, true);
                double PrevRemainingAmount = 0.0;
                double PrevPercentageVal = 0.0;//calculation Amount
                String PartyID = "";
                for (GenericValue row : partyId_list) {
                    PrevRemainingAmount = Double.parseDouble(row.get("remainingAmount").toString());
                    PrevPercentageVal += Double.parseDouble(row.get("calculationAmount").toString());
                    PartyID = row.get("partyId").toString();
                }
                //

                double newAmount = ((percentageVal * (double) list.get(i).get("amount")) / 100);
                double remainungAmount = Math.abs(PrevRemainingAmount - newAmount);

                // First Fill
                if (partyId_list.isEmpty()) {
                    if (newAmount > (double) list.get(i).get("remainingAmount")) {
                        gvValue.set("newAmount", (double) list.get(i).get("remainingAmount"));
                        gvValue.set("remainingAmount", 0.0);
                        gvValue.set("calculationAmount", 100.0);
                    } else {
                        gvValue.set("newAmount", newAmount);
                        gvValue.set("remainingAmount", remainungAmount);
                        gvValue.set("calculationAmount", percentageVal);
                    }
                    // second fill
                } else {
                    if (newAmount <= PrevRemainingAmount) {
                        gvValue.set("newAmount", newAmount);
                        gvValue.set("remainingAmount", Math.abs(PrevRemainingAmount - newAmount));
                        gvValue.set("calculationAmount", percentageVal);
                        if (remainungAmount == 0.0) {
                            gvValue.set("calculationAmount", 0.0);
                        } else {
                            gvValue.set("calculationAmount", percentageVal);
                        }
                    } else {
                        gvValue.set("newAmount", PrevRemainingAmount);
                        gvValue.set("remainingAmount", 0.0);
                        if (remainungAmount == 0.0) {
                            gvValue.set("calculationAmount", 0.0);
                        } else {
                            gvValue.set("calculationAmount", Math.abs(PrevPercentageVal - 100));
                        }
                    }
                }

//                if (partyId_list.isEmpty()) {
//                    gvValue.set("calculationAmount", percentageVal);
//                } else {
//                    if (percentageVal > (100 - PrevPercentageVal)) {
//                        gvValue.set("calculationAmount", Math.abs(PrevPercentageVal - 100));
//                    } else {
//                        gvValue.set("calculationAmount", percentageVal);
//                    }
//                }
                gvValue.set("updatedFlag", "FROM_DEPT_ALLOW");
                gvValue.store();

            }

        } catch (GenericEntityException ex) {
            try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
                return "error";
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                return "error";
            }
        } finally {
            // only commit the transaction if we started one... this will throw an exception if it fails
            try {
                TransactionUtil.commit(beganTransaction);

            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not commit transaction for entity engine error occurred while saving abandoned cart information", module);
                return "error";
            }
        }
        return "";
    }

    public static String post_ControlPartaily(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        String partyId = request.getParameter("partyId");
        String dept = request.getParameter("dept");

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("isPosted", "N");

        try {
            List<GenericValue> list = delegator.findList("EmplSalaryPartialy", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!list.isEmpty()) {
                String returnMessage = "";
//                String returnMessage = post_AcctgTransTotaly(request, response, "P");
                if (returnMessage.equals("success")) {
                    for (GenericValue row : list) {
                        GenericValue gvValue = (GenericValue) row.clone();
                        gvValue.set("isPosted", "Y");
                        gvValue.store();
                    }

                    List<GenericValue> EmplSalary_list = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
                    for (GenericValue row : EmplSalary_list) {
                        GenericValue gvValue = (GenericValue) row.clone();
                        gvValue.set("isPosted", "Y");
                        gvValue.set("postedType", "FROM_PARTIALY");
                        gvValue.store();
                    }
                } else {// end inner if 
                    return "error";
                }
            } else {  // end outer if
                return "error";

            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return "success";
    }

    public static String updateEmplSalaryPartaily(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
//        Long seq = Long.parseLong(request.getParameter("seq"), 10);
        String partyId = request.getParameter("partyId");
//        String dept = request.getParameter("dept");
        String allowenceId = request.getParameter("allowenceId");
        String emplPartailyId = request.getParameter("emplPartailyId");
        String userLoginId = request.getParameter("userLoginId");
        String updatedFlag = request.getParameter("updatedFlag");

        String calculationType = request.getParameter("calculationType");
        //
        double calculationAmount = Double.parseDouble(request.getParameter("calculationAmount"));
        double newAmount = Double.parseDouble(request.getParameter("newAmount"));
        double remainingAmount = Double.parseDouble(request.getParameter("remainingAmount"));
        //
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("emplPartailyId", emplPartailyId);

        try {
            List<GenericValue> list = delegator.findList("EmplSalaryPartialy", EntityCondition.makeCondition(criteria), null, null, null, true);

            for (GenericValue row : list) {
                GenericValue gvValue = (GenericValue) row.clone();
                gvValue.set("userLoginId", userLoginId);
                gvValue.set("updatedFlag", updatedFlag);
                gvValue.set("calculationType", calculationType);

                //TO Get Previous remaining Amount to Validation
                Map<String, Object> partyId_criteria = new HashMap<String, Object>();
                partyId_criteria.put("partyId", row.get("partyId"));
                partyId_criteria.put("allowenceId", row.get("allowenceId"));
                Long seq_partyId_criteria = (Long) row.get("seq");
//                partyId_criteria.put("seq", (seq_partyId_criteria - 1));

                List<String> listOrder = new ArrayList<>();
                listOrder.add("emplPartailyId");

                // previous Values List
                List<GenericValue> partyId_list = delegator.findList("EmplSalaryPartialy", EntityCondition.makeCondition(partyId_criteria), null, listOrder, null, true);
                double PrevRemainingAmount = 0.0;
                double PrevcalculationAmount = 0.0;//calculation Amount
                double amount = 0.0;
                String PartyID = "";
                for (GenericValue partyId_list_row : partyId_list) {
                    PrevRemainingAmount = Double.parseDouble(partyId_list_row.get("remainingAmount").toString());
                    PrevcalculationAmount += Double.parseDouble(partyId_list_row.get("calculationAmount").toString());
                    amount = Double.parseDouble(partyId_list_row.get("amount").toString());
                    PartyID = partyId_list_row.get("partyId").toString();
                }
                //
                double remaining_Amount = Math.abs((double) row.get("amount") - newAmount);
                // First update
                if (partyId_list.isEmpty()) {
                    if (newAmount > (double) row.get("amount")) {
                        gvValue.set("newAmount", (double) row.get("amount"));
                        gvValue.set("remainingAmount", 0.0);
                        gvValue.set("calculationAmount", 100.0);
                    } else {
                        gvValue.set("newAmount", newAmount);
                        gvValue.set("remainingAmount", remaining_Amount);
                        gvValue.set("calculationAmount", calculationAmount);
                    }

                } else {
                    if (newAmount <= PrevRemainingAmount) {

                        gvValue.set("newAmount", newAmount);
                        gvValue.set("remainingAmount", Math.abs(PrevRemainingAmount - newAmount));
                        gvValue.set("calculationAmount", calculationAmount);
                    } else {
                        gvValue.set("newAmount", (double) row.get("newAmount"));
                        gvValue.set("remainingAmount", (double) row.get("remainingAmount"));
                        if (PrevRemainingAmount == 0.0) {
                            gvValue.set("calculationAmount", (double) row.get("calculationAmount"));
                        } else {
                            gvValue.set("calculationAmount", Math.abs(PrevcalculationAmount - 100));
                        }
                    }

                }
                gvValue.store();

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String post_ControlTotaly(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;

        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        String jobGroup = request.getParameter("jobGroup").toString();

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("isPosted", "N");

        boolean beganTransaction = false;
        try {
            beganTransaction = TransactionUtil.begin();
            List<GenericValue> list = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);

            if (!list.isEmpty()) {
                Map<String, Object> returnMessageMap = new HashMap<String, Object>();
                returnMessageMap = post_AcctgTransTotaly(request, response);
                String returnMessage = (String) returnMessageMap.get("responseMessage");
                if (returnMessage.equals("success")) {
                    for (GenericValue row : list) {
                        GenericValue gvValue = (GenericValue) row.clone();
                        gvValue.set("isPosted", "Y");
                        gvValue.set("postedType", "FROM_TOTALY");
                        gvValue.store();
                    }
                } else {
                    return "error";
                }
            }// End if 
            else {
                return "error";

            }

        } catch (Exception ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
            try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
                return "error";
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
            }
        } finally {
            // only commit the transaction if we started one... this will throw an exception if it fails
            try {
                TransactionUtil.commit(beganTransaction);
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not commit transaction for entity engine error occurred while saving abandoned cart information", module);
                return "error";
            }
        }
        return "success";
    }

    public static String post_AcctgTransTotaly_fromJava(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        String returnMessage = "success";

        Set<String> organization_Distinct = new HashSet<String>();
        Set<String> allowences_Distinct = new HashSet<String>();
        int acctgTransEntrySeqNum = 0;
        String acctgTransEntrySeqId = UtilFormatOut.formatPaddedNumber(acctgTransEntrySeqNum, TRANS_ITEM_SEQUENCE_ID_DIGITS);
        Long year = Long.parseLong(request.getParameter("year"), 10);
//        long year1 =Long.valueOf(request.getParameter("year"));
        Long month = Long.parseLong(request.getParameter("month"), 10);
        String monthString = "";
        BigDecimal debit = new BigDecimal(0);
        BigDecimal credit = new BigDecimal(0);

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        String userLoginId = (String) userLogin.getString("userLoginId");

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("year", year);
        criteria.put("month", month);
//        criteria.put("isPosted", "Y");

        Map<String, Object> criteriaCurrency = new HashMap<String, Object>();
        criteriaCurrency.put("partyId", "Company");

        Map<String, String> criteriaSetSocialSec = new HashMap<String, String>();
        criteriaSetSocialSec.put("Key", "SOCIAL_SECURITY_ALLOWENCES_ID");

        Map<String, String> criteriaBasicSalary = new HashMap<String, String>();
        criteriaBasicSalary.put("Key", "BASIC_SALARY_ALLOWENCES_ID");

        Map<String, String> criteriaTax = new HashMap<String, String>();
        criteriaTax.put("Key", "INCOME_TAX_ALLOWENCES_ID");

        List<GenericValue> toBeStored = new LinkedList<GenericValue>();
        GenericValue acctgTrans = null;
        GenericValue acctgTransEntry = null;
        boolean beganTransaction = false;
        try {
            beganTransaction = TransactionUtil.begin();
            GenericValue resultSocialSec = delegator.findOne("GlobalPayrollSettings", criteriaSetSocialSec, true);
            String socialSec = (String) resultSocialSec.get("Value");

            GenericValue resultTax = delegator.findOne("GlobalPayrollSettings", criteriaTax, true);
            String TaxId = (String) resultTax.get("Value");

            GenericValue resultBasicSalary = delegator.findOne("GlobalPayrollSettings", criteriaBasicSalary, true);
            String basicSalaryId = (String) resultBasicSalary.get("Value");

            Map<String, String> criteriaAllowBasicSalary = new HashMap<String, String>();
            criteriaAllowBasicSalary.put("allowenceId", basicSalaryId);

            GenericValue resultBasicSalGl = delegator.findOne("Allowences", criteriaAllowBasicSalary, true);
            String basicSalaryGl = (String) resultBasicSalGl.get("glAccountIdComp");

            GenericValue resultCurrency = delegator.findOne("PartyAcctgPreference", criteriaCurrency, true);
            String origCurrencyUomId = (String) resultCurrency.get("baseCurrencyUomId");

            List<GenericValue> listEmplSalary = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);

            acctgTrans = delegator.makeValue("AcctgTrans");
            String acctgTransId = delegator.getNextSeqId("AcctgTrans");///make-next-seq-id
            monthString = new DateFormatSymbols().getMonths()[month.intValue() - 1];
            String description = "Salaries Entry for " + monthString + " " + year;

            System.out.println("acctgTransId == " + acctgTransId);
            acctgTrans.put("acctgTransId", acctgTransId);
            acctgTrans.put("acctgTransCode", delegator.getNextSeqId("AcctgTransCode"));
            acctgTrans.put("acctgTransTypeId", "_NA_");
            acctgTrans.put("description", description);
            acctgTrans.put("transactionDate", timestamp);
            acctgTrans.put("postedDate", timestamp);
            acctgTrans.put("isPosted", "Y");// edite
            acctgTrans.put("partyId", "Company");
            acctgTrans.put("year", year.toString());
            acctgTrans.put("createdByUserLogin", userLoginId);
            acctgTrans.put("lastModifiedByUserLogin", userLoginId);
            toBeStored.add(acctgTrans);

            for (GenericValue row : listEmplSalary) {
                organization_Distinct.add(row.get("organizationPartyId").toString());
                allowences_Distinct.add(row.get("allowenceId").toString());
            }

            for (String itemOrg : organization_Distinct) {
                BigDecimal SSE = new BigDecimal(0);
                BigDecimal netSalary = new BigDecimal(0);
                String glSSE = "";
                criteria.put("organizationPartyId", itemOrg);
                List<GenericValue> listEmplSalarySub = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
                Map<String, List<String>> values = new HashMap<String, List<String>>();
                List<String> subList = new ArrayList<String>();
                for (GenericValue row : listEmplSalarySub) {
                    subList = new ArrayList<String>();
                    String allow = row.get("allowenceId").toString();
                    String amount = row.get("amount").toString();
                    String isCompany = row.get("isCompany").toString();
                    String glAccountId = row.get("glAccountId").toString();

                    if (values.isEmpty()) {
                        subList.add(amount);
                        subList.add(glAccountId);
                        values.put(allow, subList);
                    } else {
                        if (values.containsKey(allow)) {
//                            double newAmount = Double.parseDouble(amount) + Double.parseDouble(values.get(allow).get(0));
                            BigDecimal oldAmount = new BigDecimal(amount);
                            BigDecimal subAmount = new BigDecimal(values.get(allow).get(0));
                            subList.add((oldAmount.add(subAmount)).toString());
                            if (allow.equals(socialSec) && isCompany.equals("N")) {
                                subList.add(glAccountId);
                            } else if (!allow.equals(socialSec)) {
                                subList.add(glAccountId);
                            } else if (allow.equals(socialSec)) {
                                subList.add(values.get(allow).get(1));
                            }
                            values.put(allow, subList);
                        } else {
                            subList.add(amount);
                            subList.add(glAccountId);
                            values.put(allow, subList);
                        }
                    }

                    if (allow.equals(socialSec) && isCompany.equals("Y")) {
                        BigDecimal subSSE = new BigDecimal(amount);
                        SSE = SSE.add(subSSE);
//                        SSE = SSE + Double.parseDouble(amount);
                        glSSE = glAccountId;
                    }

                    if (!isCompany.equals("Y")) {
                        BigDecimal subNetSalary = new BigDecimal(amount);
                        netSalary = netSalary.add(subNetSalary);
//                        netSalary = netSalary + Double.parseDouble(amount);
                    }
                } // end for of listEmplSalarySub

                subList = new ArrayList<String>();
                subList.add(netSalary.toString());
                subList.add(basicSalaryGl);
                values.put("Net_Salary", subList);

                if (SSE.compareTo(BigDecimal.ZERO) != 0) {
                    subList = new ArrayList<String>();
                    subList.add((SSE.abs()).toString());
                    subList.add(glSSE);
                    values.put("SSE", subList);
                }
                for (String key : values.keySet()) {
                    acctgTransEntry = delegator.makeValue("AcctgTransEntry");
//                    acctgTransEntrySeqId = Integer.toString(Integer.parseInt(acctgTransEntrySeqId) + 00001);
//                     increment the counter
                    acctgTransEntrySeqNum++;
                    acctgTransEntrySeqId = UtilFormatOut.formatPaddedNumber(acctgTransEntrySeqNum, TRANS_ITEM_SEQUENCE_ID_DIGITS);

                    String allowDescription = "";
                    if (!key.equals("SSE") && !key.equals("Net_Salary")) {
                        Map<String, String> criteriaAllowDescription = new HashMap<String, String>();
                        criteriaAllowDescription.put("allowenceId", key);
                        GenericValue resultAllowDescription = delegator.findOne("Allowences", criteriaAllowDescription, true);
                        allowDescription = (String) resultAllowDescription.get("description");
                    }

                    if (key.equals(socialSec) || key.equals(TaxId) || key.equals("Net_Salary") || Double.parseDouble(values.get(key).get(0)) < 0) {
                        BigDecimal subCredit = new BigDecimal(values.get(key).get(0)).abs();
                        credit = credit.add(subCredit);

                        acctgTransEntry.put("acctgTransId", acctgTransId);
                        acctgTransEntry.put("acctgTransEntrySeqId", acctgTransEntrySeqId);//seq by parent
                        acctgTransEntry.put("debitCreditFlag", "C");
                        acctgTransEntry.put("acctgTransEntryTypeId", "_NA_");
                        acctgTransEntry.put("amount", new BigDecimal(values.get(key).get(0)).abs()); // edite
                        acctgTransEntry.put("origAmount", new BigDecimal(values.get(key).get(0)).abs()); // edite
                        acctgTransEntry.put("partyId", itemOrg);
                        acctgTransEntry.put("organizationPartyId", "Company");
                        acctgTransEntry.put("currencyUomId", origCurrencyUomId);
                        acctgTransEntry.put("origCurrencyUomId", origCurrencyUomId);
                        acctgTransEntry.put("glAccountId", values.get(key).get(1)); // GL
                        if (key.equals("Net_Salary")) {
                            acctgTransEntry.put("description", key);
                        } else {
                            acctgTransEntry.put("description", allowDescription);
                        }
                        toBeStored.add(acctgTransEntry);
//                        acctgTransEntry.create();
                    } else {
                        BigDecimal subDebit = new BigDecimal(values.get(key).get(0)).abs();
                        debit = debit.add(subDebit);

                        acctgTransEntry.put("acctgTransId", acctgTransId);
                        acctgTransEntry.put("acctgTransEntrySeqId", acctgTransEntrySeqId);//seq by parent
                        acctgTransEntry.put("debitCreditFlag", "D");
                        acctgTransEntry.put("acctgTransEntryTypeId", "_NA_");
                        acctgTransEntry.put("amount", new BigDecimal(values.get(key).get(0)).abs());
                        acctgTransEntry.put("origAmount", new BigDecimal(values.get(key).get(0)).abs()); //edite
                        acctgTransEntry.put("partyId", itemOrg);
                        acctgTransEntry.put("organizationPartyId", "Company");
                        acctgTransEntry.put("currencyUomId", origCurrencyUomId);
                        acctgTransEntry.put("origCurrencyUomId", origCurrencyUomId);
                        acctgTransEntry.put("glAccountId", values.get(key).get(1)); // GL
                        if (key.equals("SSE")) {
                            acctgTransEntry.put("description", key);
                        } else {
                            acctgTransEntry.put("description", allowDescription);
                        }
                        toBeStored.add(acctgTransEntry);

//                        acctgTransEntry.create();
                    }
//                    throw new Exception("Test rol back");
                }//end for values map
            } // end for of organization_Distinct
            if (returnMessage.equals("success")) {
                if (debit.compareTo(credit) == 0) {
                    System.out.println("compare");
                    System.out.println("debit " + debit);
                    System.out.println("credit " + credit);
                    try {
                        delegator.storeAll(toBeStored);
                    } catch (Exception e) {
                        returnMessage = "error";
                        e.getMessage();
                    }
                } else {
                    System.out.println("not compare");
                    System.out.println("debit " + debit);
                    System.out.println("credit " + credit);
                    returnMessage = "error";
                }
            }
        } catch (Exception ex) {
            returnMessage = "error";
//            delegator.rollback();
            ex.printStackTrace();
            try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
            }
        } finally {
            // only commit the transaction if we started one... this will throw an exception if it fails
            try {
                TransactionUtil.commit(beganTransaction);
//                returnMessage = "success";
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not commit transaction for entity engine error occurred while saving abandoned cart information", module);
                returnMessage = "error";
            }
        }//finaly

        return returnMessage;
    }

    public static Map<String, Object> post_AcctgTransTotaly(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        String returnMessage = "success";
        Map<String, Object> returnMessageMap = new HashMap<String, Object>();

        Set<String> organization_Distinct = new HashSet<String>();
        Set<String> allowences_Distinct = new HashSet<String>();
        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        String jobGroup = request.getParameter("jobGroup").toString();
        String monthString = "";
        BigDecimal debit = new BigDecimal(0);
        BigDecimal credit = new BigDecimal(0);

//        Date date = new Date();
//        Timestamp timestamp = new Timestamp(date.getTime());
        LocalDate calculationStartDate = LocalDate.of(Integer.valueOf(year.toString()), Integer.valueOf(month.toString()), 1);
        int lengthOfMonth = calculationStartDate.lengthOfMonth();
        LocalDate calculationEndDate = LocalDate.of(Integer.valueOf(year.toString()), Integer.valueOf(month.toString()), lengthOfMonth);
        Timestamp endDateTimestamp = PersonUtilServices.convertStringToTimestamp(calculationEndDate.toString(), false);

        GenericValue userLogin = (GenericValue) request.getSession().getAttribute("userLogin");
        String userLoginId = (String) userLogin.getString("userLoginId");

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("jobGroupId", jobGroup);
//        criteria.put("isPosted", "Y");

        Map<String, Object> criteriaCurrency = new HashMap<String, Object>();
        criteriaCurrency.put("partyId", "Company");

        Map<String, String> criteriaSetSocialSec = new HashMap<String, String>();
        criteriaSetSocialSec.put("Key", "SOCIAL_SECURITY_ALLOWENCES_ID");

        Map<String, String> criteriaBasicSalary = new HashMap<String, String>();
        criteriaBasicSalary.put("Key", "BASIC_SALARY_ALLOWENCES_ID");

        Map<String, String> criteriaTax = new HashMap<String, String>();
        criteriaTax.put("Key", "INCOME_TAX_ALLOWENCES_ID");

        boolean beganTransaction = false;
        try {
            beganTransaction = TransactionUtil.begin();
            GenericValue resultSocialSec = delegator.findOne("GlobalPayrollSettings", criteriaSetSocialSec, true);
            String socialSec = (String) resultSocialSec.get("Value");

            GenericValue resultTax = delegator.findOne("GlobalPayrollSettings", criteriaTax, true);
            String TaxId = (String) resultTax.get("Value");

            GenericValue resultBasicSalary = delegator.findOne("GlobalPayrollSettings", criteriaBasicSalary, true);
            String basicSalaryId = (String) resultBasicSalary.get("Value");

            Map<String, String> criteriaAllowBasicSalary = new HashMap<String, String>();
            criteriaAllowBasicSalary.put("allowenceId", basicSalaryId);

            GenericValue resultBasicSalGl = delegator.findOne("Allowences", criteriaAllowBasicSalary, true);
            String basicSalaryGl = (String) resultBasicSalGl.get("glAccountIdComp");

            GenericValue resultCurrency = delegator.findOne("PartyAcctgPreference", criteriaCurrency, true);
            String origCurrencyUomId = (String) resultCurrency.get("baseCurrencyUomId");

            List<GenericValue> listEmplSalary = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);

            monthString = new DateFormatSymbols().getMonths()[month.intValue() - 1];
            String description = "Salaries Entry for " + monthString + " " + year;

            // xml service
            Map<String, Object> acctgTransPost = new HashMap<String, Object>();
            acctgTransPost.put("acctgTransId", "");
            acctgTransPost.put("acctgTransTypeId", "_NA_");
            acctgTransPost.put("description", description);
            acctgTransPost.put("transactionDate", endDateTimestamp);
            acctgTransPost.put("year", year.toString());
            acctgTransPost.put("partyId", "Company");
            acctgTransPost.put("glFiscalTypeId", "ACTUAL");
            acctgTransPost.put("userLogin", userLogin);
//            acctgTransPost.put("login-required", "false");

            Map<String, Object> acctgTransResults = dispatcher.runSync("createAcctgTrans", acctgTransPost);
            String acctgTransId = (String) acctgTransResults.get("acctgTransId");
            returnMessage = (String) acctgTransResults.get("responseMessage");
            returnMessageMap = acctgTransResults;
            //xml service

            for (GenericValue row : listEmplSalary) {
                organization_Distinct.add(row.get("organizationPartyId").toString());
                allowences_Distinct.add(row.get("allowenceId").toString());
            }
            System.out.println("**organization_Distinct " + organization_Distinct);
            for (String itemOrg : organization_Distinct) {
                BigDecimal SSE = new BigDecimal(0);
                BigDecimal netSalary = new BigDecimal(0);
                String glSSE = "";
                criteria.put("organizationPartyId", itemOrg);
                List<GenericValue> listEmplSalarySub = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);

                Map<String, List<String>> values = new HashMap<String, List<String>>();
                List<String> subList = new ArrayList<String>();
                for (GenericValue row : listEmplSalarySub) {
                    subList = new ArrayList<String>();
                    String allow = row.get("allowenceId").toString();
                    String amount = row.get("amount").toString();
                    String isCompany = row.get("isCompany").toString();
                    String glAccountId = row.get("glAccountId").toString();

                    if (values.isEmpty()) {
                        subList.add(amount);
                        subList.add(glAccountId);
                        values.put(allow, subList);
                    } else {
                        if (values.containsKey(allow)) {
                            BigDecimal oldAmount = new BigDecimal(amount);
                            BigDecimal subAmount = new BigDecimal(values.get(allow).get(0));
                            subList.add((oldAmount.add(subAmount)).toString());
                            if (allow.equals(socialSec) && isCompany.equals("N")) {
                                subList.add(glAccountId);
                            } else if (!allow.equals(socialSec)) {
                                subList.add(glAccountId);
                            } else if (allow.equals(socialSec)) {
                                subList.add(values.get(allow).get(1));
                            }
                            values.put(allow, subList);
                        } else {
                            subList.add(amount);
                            subList.add(glAccountId);
                            values.put(allow, subList);
                        }
                    }

                    if (allow.equals(socialSec) && isCompany.equals("Y")) {
                        BigDecimal subSSE = new BigDecimal(amount);
                        SSE = SSE.add(subSSE);
                        glSSE = glAccountId;
                    }

                    if (!isCompany.equals("Y")) {
                        BigDecimal subNetSalary = new BigDecimal(amount);
                        netSalary = netSalary.add(subNetSalary);
                    }
                } // end for of listEmplSalarySub

                subList = new ArrayList<String>();
                subList.add(netSalary.toString());
                subList.add(basicSalaryGl);
                values.put("Net_Salary", subList);

                if (SSE.compareTo(BigDecimal.ZERO) != 0) {
                    subList = new ArrayList<String>();
                    subList.add((SSE.abs()).toString());
                    subList.add(glSSE);
                    values.put("SSE", subList);
                }

                for (String key : values.keySet()) {

                    String allowDescription = "";
                    if (!key.equals("SSE") && !key.equals("Net_Salary")) {
                        Map<String, String> criteriaAllowDescription = new HashMap<String, String>();
                        criteriaAllowDescription.put("allowenceId", key);
                        GenericValue resultAllowDescription = delegator.findOne("Allowences", criteriaAllowDescription, true);
                        allowDescription = (String) resultAllowDescription.get("description");
                    }

                    if (key.equals(socialSec) || key.equals(TaxId) || key.equals("Net_Salary") || Double.parseDouble(values.get(key).get(0)) < 0) {
                        BigDecimal subCredit = new BigDecimal(values.get(key).get(0)).abs();
                        credit = credit.add(subCredit);
                        Map<String, Object> acctgTransEntryPost = new HashMap<String, Object>();

                        if (key.equals("Net_Salary")) {
                            acctgTransEntryPost.put("description", key);
                        } else {
                            acctgTransEntryPost.put("description", allowDescription);
                        }

                        acctgTransEntryPost.put("acctgTransId", acctgTransId);
                        acctgTransEntryPost.put("acctgTransEntrySeqId", "");//seq by parent
                        acctgTransEntryPost.put("debitCreditFlag", "C");
                        acctgTransEntryPost.put("acctgTransEntryTypeId", "_NA_");
                        acctgTransEntryPost.put("amount", new BigDecimal(values.get(key).get(0)).abs()); // edite
                        acctgTransEntryPost.put("origAmount", new BigDecimal(values.get(key).get(0)).abs()); // edite
                        acctgTransEntryPost.put("partyId", itemOrg);
                        acctgTransEntryPost.put("organizationPartyId", "Company");
                        acctgTransEntryPost.put("currencyUomId", origCurrencyUomId);
                        acctgTransEntryPost.put("origCurrencyUomId", origCurrencyUomId);
                        acctgTransEntryPost.put("glAccountId", values.get(key).get(1)); // GL
                        acctgTransEntryPost.put("userLogin", userLogin);
                        acctgTransEntryPost.put("extInfo", "J");

                        Map<String, Object> acctgTransEntryResults = dispatcher.runSync("createAcctgTransEntry", acctgTransEntryPost);
                        returnMessage = (String) acctgTransEntryResults.get("responseMessage");
                        returnMessageMap = acctgTransEntryResults;
                        returnMessageMap.put("acctgTransId", acctgTransId);
                    } else {
                        BigDecimal subDebit = new BigDecimal(values.get(key).get(0)).abs();
                        debit = debit.add(subDebit);
                        Map<String, Object> acctgTransEntryPost = new HashMap<String, Object>();

                        if (key.equals("SSE")) {
                            acctgTransEntryPost.put("description", key);
                        } else {
                            acctgTransEntryPost.put("description", allowDescription);
                        }

                        acctgTransEntryPost.put("acctgTransId", acctgTransId);
                        acctgTransEntryPost.put("acctgTransEntrySeqId", "");//seq by parent
                        acctgTransEntryPost.put("debitCreditFlag", "D");
                        acctgTransEntryPost.put("acctgTransEntryTypeId", "_NA_");
                        acctgTransEntryPost.put("amount", new BigDecimal(values.get(key).get(0)).abs());
                        acctgTransEntryPost.put("origAmount", new BigDecimal(values.get(key).get(0)).abs()); //edite
                        acctgTransEntryPost.put("partyId", itemOrg);
                        acctgTransEntryPost.put("organizationPartyId", "Company");
                        acctgTransEntryPost.put("currencyUomId", origCurrencyUomId);
                        acctgTransEntryPost.put("origCurrencyUomId", origCurrencyUomId);
                        acctgTransEntryPost.put("glAccountId", values.get(key).get(1)); // GL
                        acctgTransEntryPost.put("userLogin", userLogin);
                        acctgTransEntryPost.put("extInfo", "J");

                        Map<String, Object> acctgTransEntryResults = dispatcher.runSync("createAcctgTransEntry", acctgTransEntryPost);
                        returnMessage = (String) acctgTransEntryResults.get("responseMessage");
                        returnMessageMap = acctgTransEntryResults;
                        returnMessageMap.put("acctgTransId", acctgTransId);
                    }
//                    throw new Exception("Test rol back");
                }//end for values map
            } // end for of organization_Distinct
            if (returnMessage.equals("success")) {
                Map<String, Object> acctgTransPostService = new HashMap<String, Object>();
                acctgTransPostService.put("acctgTransId", acctgTransId);
                acctgTransPostService.put("organizationPartyId", "Company");
                acctgTransPostService.put("partyId", "Company");
                acctgTransPostService.put("userLogin", userLogin);

                Map<String, Object> acctgTransPostResult = dispatcher.runSync("postAcctgTrans", acctgTransPostService);
                if (acctgTransPostResult.containsKey("successMessageList")) {

                    // save acctgTransId in table EmplSalaryAcctgTrans
                    Map<String, Object> emplSalaryAcctgTrans = new HashMap<String, Object>();
                    emplSalaryAcctgTrans.put("acctgTransId", acctgTransId);
                    String acctgTransCode = getAcctgTransCode_By_acctgTransId(request, response, acctgTransId);
                    emplSalaryAcctgTrans.put("acctgTransCode", acctgTransCode);
                    emplSalaryAcctgTrans.put("year", year);
                    emplSalaryAcctgTrans.put("month", month);
                    emplSalaryAcctgTrans.put("jobGroupId", jobGroup);
                    emplSalaryAcctgTrans.put("userLogin", userLogin);
                    Map<String, Object> emplSalaryAcctgTransResult = dispatcher.runSync("createEmplSalaryAcctgTrans", emplSalaryAcctgTrans);

                    returnMessage = (String) emplSalaryAcctgTransResult.get("responseMessage");
                    returnMessageMap = acctgTransPostResult;
                    returnMessageMap.put("responseMessage", emplSalaryAcctgTransResult.get("responseMessage").toString());
                    returnMessageMap.put("acctgTransId", acctgTransId);
                } else {

                    // save acctgTransId in table EmplSalaryAcctgTrans
                    Map<String, Object> emplSalaryAcctgTrans = new HashMap<String, Object>();
                    emplSalaryAcctgTrans.put("acctgTransId", acctgTransId);
                    String acctgTransCode = getAcctgTransCode_By_acctgTransId(request, response, acctgTransId);
                    emplSalaryAcctgTrans.put("acctgTransCode", acctgTransCode);
                    emplSalaryAcctgTrans.put("year", year);
                    emplSalaryAcctgTrans.put("month", month);
                    emplSalaryAcctgTrans.put("jobGroupId", jobGroup);
                    emplSalaryAcctgTrans.put("userLogin", userLogin);
                    Map<String, Object> emplSalaryAcctgTransResult = dispatcher.runSync("createEmplSalaryAcctgTrans", emplSalaryAcctgTrans);

                    returnMessage = (String) emplSalaryAcctgTransResult.get("responseMessage");
                    returnMessageMap = acctgTransPostResult;
                    returnMessageMap.put("responseMessage", emplSalaryAcctgTransResult.get("responseMessage").toString());
                    returnMessageMap.put("acctgTransId", acctgTransId);
                }

                if (debit.compareTo(credit) == 0) {
                    System.out.println("compare");
                    System.out.println("debit " + debit);
                    System.out.println("credit " + credit);
                    try {
//                        delegator.storeAll(toBeStored);
                    } catch (Exception e) {
                        returnMessage = "error";
                        e.getMessage();
                    }
                } else {
                    System.out.println("not compare");
                    System.out.println("debit " + debit);
                    System.out.println("credit " + credit);
                    Map<String, Object> criteriaRemove = new HashMap<String, Object>();
                    criteriaRemove.put("acctgTransId", acctgTransId);
                    int removeAcctgTrans = delegator.removeByAnd("AcctgTrans", criteriaRemove);
                    int removeAcctgTransEntry = delegator.removeByAnd("AcctgTransEntry", criteriaRemove);
                    returnMessage = "error";
                }
            }
        } catch (Exception ex) {
            returnMessage = "error";
//            delegator.rollback();
            ex.printStackTrace();
            try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
            }
        } finally {
            // only commit the transaction if we started one... this will throw an exception if it fails
            try {
                TransactionUtil.commit(beganTransaction);
//                returnMessage = "success";
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not commit transaction for entity engine error occurred while saving abandoned cart information", module);
                returnMessage = "error";
            }
        }//finaly

        return returnMessageMap;
    }

    public static String isPostedFromTotaly(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();

        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);

        criteria.put("year", year);
        criteria.put("month", month);

        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        try {
            jsonRes = new JSONObject();
            List<GenericValue> result = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (result.isEmpty()) {
                jsonRes.put("result", "false");
            } else if (result.get(0).get("isPosted").toString().equals("Y") && result.get(0).get("postedType").toString().equals("FROM_TOTALY")) {
                jsonRes.put("result", "true");
            } else {
                jsonRes.put("result", "false");
            }
            jsonData.add(jsonRes);
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    // return Date
    public static java.sql.Date getHiredDate(HttpServletRequest request, String partyId) throws ParseException, IOException {
        Map<String, String> criteria = new HashMap<String, String>();
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        criteria.put("partyId", partyId);
        criteria.put("TransType", "HIRING");
        PrintWriter out = null;
        java.sql.Date hiredDate = null;

        try {
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {

                for (GenericValue row : result) {
                    hiredDate = (java.sql.Date) row.get("TransDate");

                }
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return hiredDate;

    }

    public static int getDecimalFormat_BasicSalary(HttpServletRequest request) {

        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("Key", "DECIMAL_FORMAT_SALARY");
        criteria.put("globalKey", "DECIMAL_FORMAT");
        int result = 0;
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> list = delegator.findList("GlobalPayrollSettings", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (list.isEmpty()) {
                return -1;
            }
            result = Integer.parseInt((String) list.get(0).get("Value"));

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    //    service for backend validation
    public static boolean isPosted_Totaly(HttpServletRequest request, Long year, Long month) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("isPosted", "Y");
        try {
            List<GenericValue> result = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                return true;
            } else {
                return false;

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

    //    service for backend validation
    public static boolean isPosted_Totaly(HttpServletRequest request, String partyId, Long year, Long month, String expenseId, String expenseType) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("partyId", partyId);
        criteria.put("expenseType", expenseType);
        criteria.put("expenseId", expenseId);
        criteria.put("paymentTypeId", "PAY_TYPE_SEPARATED");
        criteria.put("isPosted", "Y");
        try {
            List<GenericValue> result = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                return true;
            } else {
                return false;

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

    //    service for backend validation
    public static boolean isPosted_Totaly(HttpServletRequest request, String partyId, Long year, Long month, String expenseId, String expenseType, String allowenceId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("partyId", partyId);
        criteria.put("expenseType", expenseType);
        criteria.put("expenseId", expenseId);
        criteria.put("paymentTypeId", "PAY_TYPE_SEPARATED");
        criteria.put("allowenceId", allowenceId);
        criteria.put("isPosted", "Y");
        try {
            List<GenericValue> result = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                return true;
            } else {
                return false;

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

    //    service for frontEnd validation
    public static String isPosted_Totaly(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();

        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);

        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("isPosted", "Y");
        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        try {
            jsonRes = new JSONObject();
            List<GenericValue> result = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                jsonRes.put("result", "true");
            } else {
                jsonRes.put("result", "false");
            }
            jsonData.add(jsonRes);
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    //    service for frontEnd validation
    public static String isPosted_Totaly_EndOfService(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();

        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        String partyId = request.getParameter("partyId");
        String expenseType = request.getParameter("expenseType");
//        String allowenceId = request.getParameter("allowenceId");
        String expenseId = request.getParameter("expenseId");

        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("partyId", partyId + "");
        criteria.put("expenseType", expenseType + "");
        criteria.put("expenseId", expenseId);
        criteria.put("paymentTypeId", "PAY_TYPE_SEPARATED");
//        if (!allowenceId.isEmpty()) {
//            criteria.put("allowenceId", allowenceId);
//        }
        criteria.put("isPosted", "Y");
        JSONObject jsonRes;
        try {
            jsonRes = new JSONObject();
            List<GenericValue> result = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                jsonRes.put("result", "true");
            } else {
                jsonRes.put("result", "false");
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonRes.toString());
            out.flush();
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String deleteEmplSalray(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        Map<String, String> messageMap;
        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        String flaq = request.getParameter("flaq");
        criteria.put("paymentTypeId", "PAY_TYPE_CASH");
        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("isPosted", "N");
        int removeAll = 0;
        try {
            if (flaq.equals("totaly")) {
                removeAll = delegator.removeByAnd("EmplSalary", criteria);
                IncomeTaxServices.deleteIncomeTaxAllowence(request, Integer.valueOf(request.getParameter("month")), Integer.valueOf(request.getParameter("year")));
            } else {
                removeAll = delegator.removeByAnd("EmplSalaryPartialy", criteria);

            }
            if (removeAll == 0) {
                messageMap = UtilMisc.toMap("errorMessage", "error");
                String errMsg = UtilProperties.getMessage(resourceError, "notCalculated", messageMap, UtilHttp.getLocale(request));
                request.setAttribute("_ERROR_MESSAGE_", errMsg);
                return "error";
            }
            messageMap = UtilMisc.toMap("successMessage", "");

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
            messageMap = UtilMisc.toMap("errorMessage", ex.getMessage());
            String errMsg = UtilProperties.getMessage(resourceError, "", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }

        String successMsg = UtilProperties.getMessage(resourceError, "deleteSuccess", messageMap, UtilHttp.getLocale(request));
        request.setAttribute("_EVENT_MESSAGE_", successMsg);
        return "success";

    }

    public static String deleteEmplSalray_Expense(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        Map<String, String> messageMap;
        Long year = Long.parseLong(request.getParameter("year"), 10);
        Long month = Long.parseLong(request.getParameter("month"), 10);
        String flaq = request.getParameter("flaq");
        String partyId = request.getParameter("partyId");
        String expenseId = request.getParameter("expenseId");
        String expenseType = request.getParameter("expenseType");

//        criteria.put("partyId", partyId);
//        criteria.put("year", new Long(year));
//        criteria.put("month", new Long(month));
        criteria.put("expenseId", expenseId);
        criteria.put("expenseType", expenseType);
        criteria.put("paymentTypeId", "PAY_TYPE_SEPARATED");
        criteria.put("isPosted", "N");
        int removeAll = 0;
        try {
            removeAll = delegator.removeByAnd("EmplSalary", criteria);
//                IncomeTaxServices.deleteIncomeTaxAllowence(request, Integer.valueOf(request.getParameter("month")), Integer.valueOf(request.getParameter("year")));
            if (removeAll == 0) {
                messageMap = UtilMisc.toMap("errorMessage", "error");
                String errMsg = UtilProperties.getMessage(resourceError, "PostedDelete", messageMap, UtilHttp.getLocale(request));
                request.setAttribute("_ERROR_MESSAGE_", errMsg);
                return "error";
            }
            messageMap = UtilMisc.toMap("successMessage", "");
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
            messageMap = UtilMisc.toMap("errorMessage", ex.getMessage());
            String errMsg = UtilProperties.getMessage(resourceError, "", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }

        String successMsg = UtilProperties.getMessage(resourceError, "deleteSuccess", messageMap, UtilHttp.getLocale(request));
        request.setAttribute("_EVENT_MESSAGE_", successMsg);
        return "success";

    }

    public static String deleteEmplSalray_Item(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        Map<String, String> messageMap;

        String emplSalaryId = request.getParameter("emplSalaryId");

        criteria.put("emplSalaryId", emplSalaryId);
        criteria.put("isPosted", "N");
        int remove = 0;
        try {
            remove = delegator.removeByAnd("EmplSalary", criteria);
//                IncomeTaxServices.deleteIncomeTaxAllowence(request, Integer.valueOf(request.getParameter("month")), Integer.valueOf(request.getParameter("year")));
            if (remove == 0) {
                messageMap = UtilMisc.toMap("errorMessage", "error");
                String errMsg = UtilProperties.getMessage(resourceError, "PostedDelete", messageMap, UtilHttp.getLocale(request));
                request.setAttribute("_ERROR_MESSAGE_", errMsg);
                return "error";
            }
            messageMap = UtilMisc.toMap("successMessage", "");
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
            messageMap = UtilMisc.toMap("errorMessage", ex.getMessage());
            String errMsg = UtilProperties.getMessage(resourceError, "", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }

        String successMsg = UtilProperties.getMessage(resourceError, "deleteSuccess", messageMap, UtilHttp.getLocale(request));
        request.setAttribute("_EVENT_MESSAGE_", successMsg);
        return "success";

    }

    public static String isCalculated(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();

        String partyId = request.getParameter("partyId");
        String allowenceId = request.getParameter("allowenceId");

        criteria.put("partyId", partyId);
        criteria.put("allowenceId", allowenceId);

        JSONObject jsonRes;
        JSONArray jsonData = new JSONArray();
        try {
            jsonRes = new JSONObject();
            List<GenericValue> result = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (result.isEmpty()) {
                jsonRes.put("result", "false");
            } else {
                jsonRes.put("result", "true");
            }
            jsonData.add(jsonRes);
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonData.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static boolean isCalculatedPositionGradation(HttpServletRequest request, String partyId, java.sql.Date transDate) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        Map<String, Object> criteria = new HashMap<String, Object>();
        long month = transDate.toLocalDate().getMonthValue();
        long year = transDate.toLocalDate().getYear();
        LocalDate calculatedDate = null;
        long year_table = 0;
        long month_table = 0;
        criteria.put("partyId", partyId);

        boolean isExist = false;
        try {
            List<GenericValue> result = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                for (GenericValue row : result) {
                    year_table = (Long) row.get("year");
                    month_table = (Long) row.get("month");
                    if (year_table >= year && month_table >= month) {

                        isExist = true;
                        break;
                    } else {
                        calculatedDate = LocalDate.of((int) year_table, (int) month_table, 1);
                        if (calculatedDate.isAfter(transDate.toLocalDate())) {

                            isExist = true;
                            break;

                        }
                    }
                }

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return isExist;
    }

    public static String isCalculated_MangmentEmployee(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        String strDate = request.getParameter("transDate");
        java.sql.Date transDate = convertFromStringToDate(strDate);
        long month_table = 0;
        long year_table = 0;
        LocalDate calculatedDate = null;
        long month = transDate.toLocalDate().getMonthValue();
        long year = transDate.toLocalDate().getYear();
        String isExist = "false";
        try {
            List<GenericValue> result = delegator.findAll("EmplSalary", true);
            if (!result.isEmpty()) {
                for (GenericValue row : result) {
                    year_table = (Long) row.get("year");
                    month_table = (Long) row.get("month");

                    if (year_table >= year && month_table >= month) {
                        System.out.println("isExist " + isExist);
                        isExist = "true";
                        break;
                    } else {
                        calculatedDate = LocalDate.of((int) year_table, (int) month_table, 1);
                        if (calculatedDate.isAfter(transDate.toLocalDate())) {
                            System.out.println("isExist " + isExist);
                            isExist = "true";
                            break;
                        }
                    }
                }
            }
//            jsonData.add(jsonRes);
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(isExist);
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String isCalculatedPositionGradation(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();

        String partyId = request.getParameter("partyId");
        String strDate = request.getParameter("transDate");
        java.sql.Date transDate = convertFromStringToDate(strDate);
        long month = transDate.toLocalDate().getMonthValue();
        long year = transDate.toLocalDate().getYear();
        LocalDate calculatedDate = null;
        System.out.println("month " + month);
        System.out.println("year " + year);
        long year_table = 0;
        long month_table = 0;
        criteria.put("partyId", partyId);
        String isExist = "false";
        try {
            List<GenericValue> result = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                for (GenericValue row : result) {
                    year_table = (Long) row.get("year");
                    month_table = (Long) row.get("month");

                    if (year_table >= year && month_table >= month) {
                        System.out.println("isExist " + isExist);
                        isExist = "true";
                        break;
                    } else {
                        calculatedDate = LocalDate.of((int) year_table, (int) month_table, 1);
                        if (calculatedDate.isAfter(transDate.toLocalDate())) {
                            System.out.println("isExist " + isExist);
                            isExist = "true";
                            break;
                        }
                    }
                }

            }
//            jsonData.add(jsonRes);

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(isExist);
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static boolean isCalculatedPositionGradation(Delegator delegator, String partyId, java.sql.Date transDate) throws ParseException, IOException {

        Map<String, Object> criteria = new HashMap<String, Object>();
        long month = transDate.toLocalDate().getMonthValue();
        long year = transDate.toLocalDate().getYear();
        LocalDate calculatedDate = null;
        long year_table = 0;
        long month_table = 0;
        criteria.put("partyId", partyId);

        boolean isExist = false;
        try {
            List<GenericValue> result = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                for (GenericValue row : result) {
                    year_table = (Long) row.get("year");
                    month_table = (Long) row.get("month");
                    if (year_table >= year && month_table >= month) {

                        isExist = true;
                        break;
                    } else {
                        calculatedDate = LocalDate.of((int) year_table, (int) month_table, 1);
                        if (calculatedDate.isAfter(transDate.toLocalDate())) {

                            isExist = true;
                            break;

                        }
                    }
                }

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return isExist;
    }

    public static java.sql.Date convertFromStringToDate(String strDate) throws ParseException, IOException {
        java.sql.Date sqlDate = null;
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf1.parse(strDate);
            sqlDate = new java.sql.Date(date.getTime());

            return sqlDate;

        } catch (Exception ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public static java.sql.Date convertFromStringToDate(String strDate, String Format) throws ParseException, IOException {
        java.sql.Date sqlDate = null;
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat(Format);
            java.util.Date date = sdf1.parse(strDate);
            sqlDate = new java.sql.Date(date.getTime());

            return sqlDate;

        } catch (Exception ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public static Map<String, Object> deleteEmplSalaryGradeAndFulfillment(DispatchContext ctx, Map<String, Object> context) throws ParseException, IOException {
        LocalDispatcher dispatcher = ctx.getDispatcher();

        Locale locale = (Locale) context.get("locale");

        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        String partyId = (String) context.get("partyId");
        String strTransDate = (String) context.get("transDate");
        //        for EMP_SALAY_GRADE Table 
        String rowId = (String) context.get("rowSeq");

        String maxSeqFulFillment = getMaxSeq_EmplPositionFullFillment(delegator, partyId, true);

        Map<String, Object> criteria = new HashMap<String, Object>();
        Map<String, Object> criteriaFulfillment = new HashMap<String, Object>();
        java.sql.Date sqlTransDate = convertFromStringToDate(strTransDate);
        String transType = getLastEmployeeTransType_EmplSalaryGrade(delegator, partyId, sqlTransDate);
        boolean beganTransaction = false;

        String maxSeq = getMaxSequence_PositionGradation(delegator, partyId, transType);
        System.out.println("maxSeq from delete Promotion/terminate " + maxSeq + "--- " + "from request " + rowId);
        criteriaFulfillment.put("emplPositionFulfillmentId", maxSeqFulFillment);
        criteriaFulfillment.put("partyId", partyId);
        if (maxSeq.equals(rowId)) {

//                criteria.put("emplSalaryGrade", maxSeq);
            boolean isExist = isCalculatedPositionGradation(delegator, partyId, sqlTransDate);
            try {
                if (!isExist) {

                    criteriaFulfillment = UtilMisc.toMap("partyId", partyId, "emplPositionFulfillmentId", maxSeqFulFillment);
                    criteria = UtilMisc.toMap("emplSalaryGrade", maxSeq);
                    Map<String, Object> deletePromotionResult = dispatcher.runSync("deleteEmplFulfillment", criteriaFulfillment);
                    if (ServiceUtil.isError(deletePromotionResult)) {
                        return ServiceUtil.returnError(UtilProperties.getMessage(resourceError,
                                "BonusError", locale), null, null, deletePromotionResult);

                    }
                    Map<String, Object> deleteEmplSalaryResult = dispatcher.runSync("deleteEmplSalaryGrade", criteria);
                    if (ServiceUtil.isError(deleteEmplSalaryResult)) {
                        return ServiceUtil.returnError(UtilProperties.getMessage(resourceError,
                                "BonusError", locale), null, null, deleteEmplSalaryResult);

                    }

//                        int remove = delegator.removeByAnd("EmplSalaryGrade", criteria);
//                        int removeFromful = delegator.removeByAnd("EmplPositionFulfillment", criteriaFulfillment);
                } else {
                    try {
                        beganTransaction = TransactionUtil.begin();
                        // only rollback the transaction if we started one...
                        TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning :Can't delete, this record is calculated"));
                        Msg = "";
                        return ServiceUtil.returnError(Msg);
                    } catch (GenericEntityException e) {
                        Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                        Msg = "";
                        return ServiceUtil.returnError(Msg);

                    }
                }

            } catch (Exception ex) {

                Logger.getLogger(PayrollServices.class
                        .getName()).log(Level.SEVERE, null, ex);
                try {

                    beganTransaction = TransactionUtil.begin();
                    // only rollback the transaction if we started one...
                    TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning : this is not last action "));
                    Msg = "";
                    return ServiceUtil.returnError(Msg);
                } catch (GenericEntityException e) {
                    Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                    Msg = "";
                    return ServiceUtil.returnError(Msg);
                }
            }
//        end if maxSeq==RowId
        }

        return ServiceUtil.returnSuccess(Msg);
    }

    public static Map<String, Object> deletePositionGradation_BONUS(DispatchContext ctx, Map<String, Object> context) throws ParseException, IOException {
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        HttpServletRequest request = (HttpServletRequest) context.get("request");
        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        String partyId = (String) context.get("partyId");
        String strTransDate = (String) context.get("transDate");
        Map<String, Object> criteria = new HashMap<String, Object>();
        String rowId = (String) context.get("rowSeq");
        boolean beganTransaction = false;
        java.sql.Date sqlTransDate = convertFromStringToDate(strTransDate);
        String transType = getLastEmployeeTransType_EmplSalaryGrade(delegator, partyId, sqlTransDate);
        System.out.println("transType " + transType);
        if (transType.equals("BONUS")) {
            String maxSeq = getMaxSequence_PositionGradation(delegator, partyId, transType);

            if (maxSeq.equals(rowId)) {
//                criteria.put("rowSeq", maxSeq);
                boolean isExist = isCalculatedPositionGradation(delegator, partyId, sqlTransDate);
                try {
                    if (!isExist) {
                        criteria = UtilMisc.toMap("emplSalaryGrade", maxSeq);
                        Map<String, Object> createBonusResult = dispatcher.runSync("deleteEmplSalaryGrade", criteria);
                        if (ServiceUtil.isError(createBonusResult)) {
                            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError,
                                    "BonusError", locale), null, null, createBonusResult);
                        }
//                        int remove = delegator.removeByAnd("EmplSalaryGrade", criteria);
                    } else {
                        try {
                            beganTransaction = TransactionUtil.begin();
                            // only rollback the transaction if we started one...
                            TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning :Can't delete, this record is calculated"));
                            Msg = "";
                            return ServiceUtil.returnError(Msg);
                        } catch (GenericEntityException e) {
                            Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                            Msg = "";
                            return ServiceUtil.returnError(Msg);

                        }
                    }

                } catch (Exception ex) {

                    Logger.getLogger(PayrollServices.class
                            .getName()).log(Level.SEVERE, null, ex);

                }
//        end if maxSeq==RowId
            }
        } else {
            try {
                beganTransaction = TransactionUtil.begin();
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning : this is not last action "));
                Msg = "Warrning : this is not last action ";
                return ServiceUtil.returnError(Msg);
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                Msg = "Warrning : this is not last action ";
                return ServiceUtil.returnError(Msg);
            }
        }
        Map<String, String> messageMap = UtilMisc.toMap("successMessage", "Success");
        Msg = "Deleted successfully";
        return ServiceUtil.returnSuccess(Msg);

    }

    //    public static String deletePositionGradation_BONUS(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
//        Delegator delegator = (Delegator) request.getAttribute("delegator");
//
//        Map<String, Object> criteria = new HashMap<String, Object>();
//
//        String partyId = request.getParameter("partyId");
//        String strTransDate = request.getParameter("transDate");
//        String rowId = request.getParameter("rowSeq");
//
//        String transType = getLastEmployeeTransType_EmplSalaryGrade(request, partyId);
//        System.out.println("transType " + transType);
//        if (transType.equals("BONUS")) {
//            String maxSeq = getMaxSequence_PositionGradation(request, partyId, transType);
//            java.sql.Date sqlTransDate = convertFromStringToDate(strTransDate);
//
//            if (maxSeq.equals(rowId)) {
//                criteria.put("emplSalaryGrade", maxSeq);
//                boolean isExist = isCalculatedPositionGradation(request, partyId, sqlTransDate);
//                try {
//                    if (!isExist) {
//                        int remove = delegator.removeByAnd("EmplSalaryGrade", criteria);
//                    }
//                    Map<String, String> messageMap = UtilMisc.toMap("successMessage", "");
//                    String successMsg = UtilProperties.getMessage(resourceError, "PasswordChangedSuccessfully", messageMap, UtilHttp.getLocale(request));
//                    request.setAttribute("_EVENT_MESSAGE_", successMsg);
//                    return "success";
//
//                } catch (Exception ex) {
//
//                    Logger.getLogger(PayrollServices.class
//                            .getName()).log(Level.SEVERE, null, ex);
//
//                }
////        end if maxSeq==RowId
//            }
//        } else {
//            Map<String, String> messageMap = UtilMisc.toMap("errorMessage", "");
//            String errMsg = UtilProperties.getMessage(resourceError, "lastBonus", messageMap, UtilHttp.getLocale(request));
//            request.setAttribute("_ERROR_MESSAGE_", errMsg);
//            return "error";
//        }
//
//        return "";
//    }
//    called form request 
    public static String getMaxSequence_PositionGradation(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        PrintWriter out = null;
        String partyId = request.getParameter("partyId");
        String transType = request.getParameter("transType");
        String maxSeq = getMaxSequence_PositionGradation(request, partyId, transType);

        try {

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(maxSeq.trim());
            out.flush();

        } catch (Exception ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    public static String getMaxSequence_PositionGradation(HttpServletRequest request, String partyId, java.sql.Date transDate) throws ParseException, IOException {
//        String actionType = getLastEmployeeTransType_EmplSalaryGrade(request, partyId);
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put("partyId", partyId);
        criteria.put("TransDate", transDate);
        HashMap mapValues = new HashMap();
        String rowValue = "";
        String maxSeq = "";
        List<String> listOrder = new ArrayList<>();
        listOrder.add("emplSalaryGrade");
        try {
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, listOrder, null, true);
            if (!result.isEmpty()) {
                for (GenericValue row : result) {
                    rowValue = (String) row.get("emplSalaryGrade");
                    mapValues.put(Integer.valueOf(rowValue), rowValue);
                }
                int maxKey = (int) Collections.max(mapValues.keySet());
                System.out.println("Max value from Key" + mapValues.get(maxKey));
                maxSeq = (String) mapValues.get(maxKey);

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return maxSeq;

    }

    //    called from java
    public static String getMaxSequence_PositionGradation(Delegator delegatorPram, String partyId, java.sql.Date transDate) throws ParseException, IOException {
//        String actionType = getLastEmployeeTransType_EmplSalaryGrade(request, partyId);
        Delegator delegator = delegatorPram;
        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put("partyId", partyId);
        criteria.put("TransDate", transDate);
        HashMap mapValues = new HashMap();
        String rowValue = "";
        String maxSeq = "";
        List<String> listOrder = new ArrayList<>();
        listOrder.add("emplSalaryGrade");
        try {
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, listOrder, null, true);
            if (!result.isEmpty()) {
                for (GenericValue row : result) {
                    rowValue = (String) row.get("emplSalaryGrade");
                    mapValues.put(Integer.valueOf(rowValue), rowValue);
                }
                int maxKey = (int) Collections.max(mapValues.keySet());
                System.out.println("Max value from Key" + mapValues.get(maxKey));
                maxSeq = (String) mapValues.get(maxKey);

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return maxSeq;

    }
//    called from java 

    public static String getMaxSequence_PositionGradation(HttpServletRequest request, String partyId, String transType) throws ParseException, IOException {
//        String actionType = getLastEmployeeTransType_EmplSalaryGrade(request, partyId);
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put("partyId", partyId);
        criteria.put("TransType", transType);
        String rowValue = "";
        String maxSeq = "";
        List<String> listOrder = new ArrayList<>();
        listOrder.add("emplSalaryGrade");
        HashMap mapValues = new HashMap();

        try {
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, listOrder, null, true);

            if (!result.isEmpty()) {
                for (GenericValue row : result) {
                    rowValue = (String) row.get("emplSalaryGrade");
                    mapValues.put(Integer.valueOf(rowValue), rowValue);
                }
                int maxKey = (int) Collections.max(mapValues.keySet());
                System.out.println("Max value from Key" + mapValues.get(maxKey));
                maxSeq = (String) mapValues.get(maxKey);

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return maxSeq;

    }

    //    called from xml
    public static String getMaxSequence_PositionGradation(Delegator delegator, String partyId, String transType) throws ParseException, IOException {
//        String actionType = getLastEmployeeTransType_EmplSalaryGrade(request, partyId);
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("partyId", partyId);
        criteria.put("TransType", transType);
        String maxSeq = "";
        List<String> listOrder = new ArrayList<>();
        listOrder.add("emplSalaryGrade");
        String rowValue = "";
        HashMap mapValues = new HashMap();
        try {
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, listOrder, null, true);

            if (!result.isEmpty()) {
                for (GenericValue row : result) {
                    rowValue = (String) row.get("emplSalaryGrade");
                    mapValues.put(Integer.valueOf(rowValue), rowValue);
                }
                int maxKey = (int) Collections.max(mapValues.keySet());
                System.out.println("Max value from Key" + mapValues.get(maxKey));
                maxSeq = (String) mapValues.get(maxKey);

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return maxSeq;

    }

    //    Trans Type : PROMOTION BONUS MOVING HIRING
    public static String getLastEmployeeTransType_EmplSalaryGrade(HttpServletRequest request, String partyId, java.sql.Date transDate) throws ParseException, IOException {
        Map<String, Object> criteria = new HashMap<String, Object>();
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        int year = transDate.toLocalDate().getYear();
        int month = transDate.toLocalDate().getMonthValue();
        java.sql.Date LocalTransDateMax = getLastActive_TransDate_Employee_SalaryCalculation(request, partyId, month, year);
        System.out.println("LocalTransDateMax " + LocalTransDateMax);
        criteria.put("partyId", partyId);
        criteria.put("TransDate", LocalTransDateMax);
        String TransType = "";
        try {
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                for (GenericValue row : result) {
                    TransType = (String) row.get("TransType");

                }
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return TransType;

    }

    //    OverLoad
    public static String getLastEmployeeTransType_EmplSalaryGrade(Delegator delegator, String partyId, java.sql.Date transDate) throws ParseException, IOException {
        Map<String, Object> criteria = new HashMap<String, Object>();

        int year = transDate.toLocalDate().getYear();
        int month = transDate.toLocalDate().getMonthValue();
        java.sql.Date LocalTransDateMax = getLastActive_TransDate_Employee_SalaryCalculation(delegator, partyId, month, year);

        criteria.put("partyId", partyId);
        criteria.put("TransDate", LocalTransDateMax);
        String TransType = "";
        try {
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                for (GenericValue row : result) {
                    TransType = (String) row.get("TransType");

                }
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return TransType;

    }

    //      overLoad
    public static java.sql.Date getLastActive_TransDate_Employee_SalaryCalculation(Delegator delegator, String partyId, int calculationMonth, int calculationYear) throws ParseException, IOException {

        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("partyId", partyId);
        LocalDate calculationStartDate = LocalDate.of(calculationYear, calculationMonth, 1);
        int lengthOfMonth = calculationStartDate.lengthOfMonth();
        LocalDate calculationEndDate = LocalDate.of(calculationYear, calculationMonth, lengthOfMonth);
        ArrayList transDateList = new ArrayList();
        java.sql.Date LocalTransDateMax = null;
        try {
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", EntityCondition.makeCondition(criteria), null, null, null, true);

            if (result.size() != 0) {
                for (GenericValue row : result) {
                    java.sql.Date transDate = (java.sql.Date) row.get("TransDate");
                    LocalDate localTransDate = transDate.toLocalDate();
                    if ((localTransDate.isEqual(calculationStartDate) || localTransDate.isAfter(calculationStartDate) || localTransDate.isBefore(calculationStartDate))
                            && (localTransDate.isEqual(calculationEndDate) || localTransDate.isBefore(calculationEndDate))) {
                        transDateList.add((java.sql.Date) row.get("TransDate"));

                    }
                }
                if (!transDateList.isEmpty()) {
                    LocalTransDateMax = (java.sql.Date) Collections.max(transDateList);

                }
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return LocalTransDateMax;

    }

    public static String getGlobalPayrollSetting(HttpServletRequest request, String key) {
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("Key", key);
        String result = "";
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> list = delegator.findList("GlobalPayrollSettings", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!list.isEmpty()) {
                for (GenericValue row : list) {
                    if (row.get("Value") != null) {
                        result = row.get("Value").toString();
                    }
                }

            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
            return "";

        }
        return result;
    }

    public static String getGlobalSetting(HttpServletRequest request, String key, String globalKey) {
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("Key", key);
        criteria.put("globalKey", globalKey);
        String result = "";
        try {
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            List<GenericValue> list = delegator.findList("GlobalPayrollSettings", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!list.isEmpty()) {
                for (GenericValue row : list) {
                    if (row.get("Value") != null) {
                        result = row.get("Value").toString();
                    }
                }

            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
            return "";

        }
        return result;
    }

    public static String deleteAllowenceEmployee_SS(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> messageMap;
        Map<String, String> criteria = new HashMap<String, String>();
        String Id = request.getParameter("Id");
        criteria.put("Id", Id);
        try {
            int remove = delegator.removeByAnd("CompanyEmployeeAllowences", criteria);
            if (remove > 0) {
                messageMap = UtilMisc.toMap("successMessage", "success");
                String errMsg = UtilProperties.getMessage(resourceError, "selectCalculationWay", messageMap, UtilHttp.getLocale(request));
                request.setAttribute("_EVENT_MESSAGE_", "deleted Successfully");
                return "success";
            } else {
                messageMap = UtilMisc.toMap("errorMessage", "error");
                String errMsg = UtilProperties.getMessage(resourceError, "selectCalculationWay", messageMap, UtilHttp.getLocale(request));
                request.setAttribute("_ERROR_MESSAGE_", "deleted Successfully");
                return "error";
            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";

    }

    public static String createAllowenceEmployee_SS(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String allowenceId = request.getParameter("allowenceId");
        String partyId = request.getParameter("partyId");
        String TransDate = request.getParameter("TransDate");
        String calculationId = request.getParameter("calculationId");
        String percentageAmount = request.getParameter("percentageAmount");
        String calculationAmount = request.getParameter("calculationAmount");
        String amount = request.getParameter("amount");

        String percentageAmount_Com = request.getParameter("percentageAmount_Com");
        String calculationAmount_Com = request.getParameter("calculationAmount_Com");
        String amount_Com = request.getParameter("amount_Com");

        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String stabilityId = request.getParameter("stabilityId");
        String userLoginId = request.getParameter("userLoginId");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        Map<String, Object> criteria = new HashMap<String, Object>();
        String idd = "";
        List<GenericValue> toBeStored = new LinkedList<GenericValue>();
        GenericValue item = delegator.makeValue("CompanyEmployeeAllowences");
        idd = delegator.getNextSeqId("CompanyEmployeeAllowences");
        item.put("Id", "S" + idd);
        item.put("typeId", "EMP");
        item.put("allowenceId", allowenceId);
        item.put("partyId", partyId);
        item.put("TransDate", java.sql.Date.valueOf(TransDate));
        item.put("calculationId", calculationId);
        item.put("percentageAmount", java.math.BigDecimal.valueOf(Double.valueOf(percentageAmount)));
        item.put("calculationAmount", java.math.BigDecimal.valueOf(Double.valueOf(calculationAmount)));
        item.put("amount", java.math.BigDecimal.valueOf(Double.valueOf(amount)));
        item.put("startDate", java.sql.Date.valueOf(startDate));
        if (endDate.isEmpty()) {
            item.put("endDate", null);
        } else {
            item.put("endDate", java.sql.Date.valueOf(endDate));
        }

        item.put("stabilityId", stabilityId);
        item.put("userLoginId", userLoginId);

        toBeStored.add(item);

        GenericValue item2 = delegator.makeValue("CompanyEmployeeAllowences");
        item2.put("Id", "S" + idd);
        item2.put("typeId", "COM");
        item2.put("allowenceId", allowenceId);
        item2.put("partyId", partyId);
        item2.put("TransDate", java.sql.Date.valueOf(TransDate));
        item2.put("calculationId", calculationId);

        item2.put("percentageAmount", java.math.BigDecimal.valueOf(Double.valueOf(percentageAmount_Com)));
        item2.put("calculationAmount", java.math.BigDecimal.valueOf(Double.valueOf(calculationAmount_Com)));
        item2.put("amount", java.math.BigDecimal.valueOf(Double.valueOf(amount_Com)));

        item2.put("startDate", java.sql.Date.valueOf(startDate));
        if (endDate.isEmpty()) {
            item.put("endDate", null);
        } else {
            item.put("endDate", java.sql.Date.valueOf(endDate));
        }
        item2.put("stabilityId", stabilityId);
        item2.put("userLoginId", userLoginId);
        toBeStored.add(item2);

        try {
            delegator.storeAll(toBeStored);
            return "success";
        } catch (GenericEntityException e) {
            e.getStackTrace();

        }
        return "success";
    }

    //    public static String updateAllowenceEmployee_SS(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
//        String Id = request.getParameter("Id");
//        String endDate = request.getParameter("endDate");
//
//        Delegator delegator = (Delegator) request.getAttribute("delegator");
//
//        Map<String, Object> criteria = new HashMap<String, Object>();
//        Map<String, Object> criteria = new HashMap<String, Object>();
//        criteria.put("year", year);
//
//        item.put("Id", Id);
//        item.put("typeId", "EMP");
//        if (endDate.isEmpty()) {
//            item.put("endDate", null);
//        } else {
//            item.put("endDate", java.sql.Date.valueOf(endDate));
//        }
//        toBeStored.add(item);
//
//        GenericValue item2 = delegator.makeValue("CompanyEmployeeAllowences");
//        item2.put("Id", Id);
//        item2.put("typeId", "COM");
//        if (endDate.isEmpty()) {
//            item.put("endDate", null);
//        } else {
//            item.put("endDate", java.sql.Date.valueOf(endDate));
//        }
//        toBeStored.add(item2);
//
//        try {
//            delegator.storeAll(toBeStored);
//            return "success";
//        } catch (GenericEntityException e) {
//            e.getStackTrace();
//
//        }
//        return "success";
//    }
    public static String updateAllowenceEmployee_SS(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        String Id = request.getParameter("Id");
        String endDate = request.getParameter("endDate");

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("Id", Id);

        boolean beganTransaction = false;
        try {
            beganTransaction = TransactionUtil.begin();
            List<GenericValue> list = delegator.findList("CompanyEmployeeAllowences", EntityCondition.makeCondition(criteria), null, null, null, true);

            if (!list.isEmpty()) {
                for (GenericValue row : list) {
                    if (row.get("typeId").equals("EMP")) {
                        GenericValue gvValue = (GenericValue) row.clone();
                        gvValue.set("endDate", java.sql.Date.valueOf(endDate));
                        gvValue.store();
                    } else {
                        GenericValue gvValue = (GenericValue) row.clone();
                        gvValue.set("endDate", java.sql.Date.valueOf(endDate));
                        gvValue.store();
                    }
                }

            }// End if 
            else {
                return "error";

            }

        } catch (Exception ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
            try {
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", ex);
                return "error";
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
            }
        } finally {
            // only commit the transaction if we started one... this will throw an exception if it fails
            try {
                TransactionUtil.commit(beganTransaction);
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not commit transaction for entity engine error occurred while saving abandoned cart information", module);
                return "error";
            }
        }
        return "success";
    }

    //    Delete Hiring Services
    public static Map<String, Object> updateEmplStatusToUnderHiring(DispatchContext ctx, Map<String, Object> context) throws SQLException, IOException, IOException, ParseException, GenericServiceException {
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        String partyId = (String) context.get("partyId");
        String strTransDate = (String) context.get("transDate");
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("partyId", partyId);
        java.sql.Date sqlTransDate = convertFromStringToDate(strTransDate);
        String transType = getLastEmployeeTransType_EmplSalaryGrade(delegator, partyId, sqlTransDate);
        boolean beganTransaction = false;
        System.out.println("transType " + transType);
        if (transType.equals("HIRING")) {

            try {

                boolean isExist = PayrollServices.isCalculatedPositionGradation(delegator, partyId, sqlTransDate);
                if (!isExist) {

                    List<GenericValue> list = delegator.findList("Person", EntityCondition.makeCondition(criteria), null, null, null, true);
                    for (int i = 0; i < list.size(); i++) {
                        GenericValue gvValue = (GenericValue) list.get(i).clone();
                        gvValue.set("EmplStatus", "UNDER_HIRING");
                        gvValue.store();
                        System.out.println("Done Updated");

                    }
                } else {
                    try {
                        beganTransaction = TransactionUtil.begin();
                        // only rollback the transaction if we started one...
                        TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning :Can't delete, this record is calculated"));
                        Msg = "";
                        return ServiceUtil.returnError(Msg);
                    } catch (GenericEntityException e) {
                        Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                        Msg = "";
                        return ServiceUtil.returnError(Msg);
                    }
                }
            } catch (GenericEntityException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            try {
                beganTransaction = TransactionUtil.begin();
                // only rollback the transaction if we started one...
                TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning : this is not last action "));
                Msg = "Warrning : this is not last action ";
                return ServiceUtil.returnError(Msg);
            } catch (GenericEntityException e) {
                Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                Msg = "Warrning : this is not last action ";
                return ServiceUtil.returnError(Msg);
            }
        }
        Map<String, String> messageMap = UtilMisc.toMap("successMessage", "Success");
        String successMsg = UtilProperties.getMessage(resourceError, "deleteSuccess", messageMap, locale);
        return ServiceUtil.returnSuccess(successMsg);
    }

    public static Map<String, Object> deleteEmployment(DispatchContext ctx, Map<String, Object> context) throws ParseException, IOException {
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Locale locale = (Locale) context.get("locale");
        String Msg = null;
        Delegator delegator = ctx.getDelegator();
        String partyId = (String) context.get("partyId");
        String strTransDate = (String) context.get("transDate");
        String maxSeqEmployment = MovingEmployee.getMaxSequence_Employment(delegator, partyId, true);
        Map<String, Object> criteriaEmployment = new HashMap<String, Object>();
        boolean beganTransaction = false;
        java.sql.Date sqlTransDate = convertFromStringToDate(strTransDate);
        criteriaEmployment.put("employmentId", maxSeqEmployment);
        criteriaEmployment.put("partyIdTo", partyId);

        boolean isExist = isCalculatedPositionGradation(delegator, partyId, sqlTransDate);
        try {
            if (!isExist) {

                criteriaEmployment = UtilMisc.toMap("partyIdTo", partyId, "employmentId", maxSeqEmployment);
                Map<String, Object> deleteEmlpoymentResult = dispatcher.runSync("deleteEmployment", criteriaEmployment);
                if (ServiceUtil.isError(deleteEmlpoymentResult)) {
                    return ServiceUtil.returnError(UtilProperties.getMessage(resourceError,
                            "BonusError", locale), null, null, deleteEmlpoymentResult);

                }

                return ServiceUtil.returnSuccess("success");
            } else {
                try {
                    beganTransaction = TransactionUtil.begin();
                    // only rollback the transaction if we started one...
                    TransactionUtil.rollback(beganTransaction, "Error saving data ", new GenericEntityException("Warrning :Can't delete, this record is calculated"));
                    Msg = "";
                    return ServiceUtil.returnError(Msg);
                } catch (GenericEntityException e) {
                    Debug.logError(e, "Could not rollback transaction: " + e.toString(), module);
                    Msg = "";
                    return ServiceUtil.returnError(Msg);
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        }
        Map<String, String> messageMap = UtilMisc.toMap("successMessage", "Success");
        String successMsg = UtilProperties.getMessage(resourceError, "deleteSuccess", messageMap, locale);
        return ServiceUtil.returnSuccess(successMsg);
    }

    public static String getGlobalPayrollSettingAndIsCalc(HttpServletRequest request, HttpServletResponse response) throws SQLException, ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        String Key = request.getParameter("Key");
        String globalKey = request.getParameter("globalKey");

        String partyId = request.getParameter("partyId");
        String allowenceId = request.getParameter("allowenceId");

        JSONObject jsonRes;
        JSONArray jsonGrade = new JSONArray();
        try {

            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("Key", "SOCIAL_SECURITY_ALLOWENCES_ID");
            criteria.put("globalKey", "SOCIAL_SECURITY");
            List<GenericValue> list = delegator.findList("GlobalPayrollSettings", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!list.isEmpty()) {
                if (list.get(0).get("Value").toString().equals(allowenceId)) {
                    System.out.println("from 1");
                    jsonRes = new JSONObject();
                    jsonRes.put("Value", "true");
                    jsonGrade.add(jsonRes);
                }
            } else {

                Map<String, String> criteria2 = new HashMap<String, String>();
                criteria2.put("Key", "INCOME_TAX_ALLOWENCES_ID");
                criteria2.put("globalKey", "INCOME_TAX");
                List<GenericValue> list2 = delegator.findList("GlobalPayrollSettings", EntityCondition.makeCondition(criteria2), null, null, null, true);
                if (!list2.isEmpty()) {

                    if (list2.get(0).get("Value").toString().equals(allowenceId)) {
                        System.out.println("from 2");
                        jsonRes = new JSONObject();
                        jsonRes.put("Value", "true");
                        jsonGrade.add(jsonRes);
                    }

                } else if (list2.isEmpty()) {
                    Map<String, String> criteria3 = new HashMap<String, String>();
                    criteria3.put("partyId", partyId);
                    criteria3.put("allowenceId", allowenceId);
                    List<GenericValue> list3 = delegator.findList("EmplSalary", EntityCondition.makeCondition(criteria3), null, null, null, true);
                    if (!list3.isEmpty()) {
                        System.out.println("from 3");
                        jsonRes = new JSONObject();
                        jsonRes.put("Value", "true");
                        jsonGrade.add(jsonRes);
                    }
                } else {
                    jsonRes = new JSONObject();
                    jsonRes.put("Value", "false");
                    jsonGrade.add(jsonRes);
                }

            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonGrade.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    //    this service check if has row in any entity
    public static String isHaveRow_Validation(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        String fieldName = request.getParameter("fieldName");
        String fieldId = request.getParameter("fieldId");
        String entityName = request.getParameter("entityName");
        criteria.put(fieldName, fieldId);
        String isExist = "false";
        try {
            List<GenericValue> result = delegator.findList(entityName, EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                isExist = "true";

            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(isExist);
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static boolean isTransTypeExsist_Validation(Delegator delegator, String partyId, java.sql.Date transDate, String transType) throws ParseException, IOException {
        boolean isExist = false;
        LocalDate startMonth = LocalDate.of(transDate.toLocalDate().getYear(), transDate.toLocalDate().getMonthValue(), 1);
        LocalDate endMonth = LocalDate.of(transDate.toLocalDate().getYear(), transDate.toLocalDate().getMonthValue(), startMonth.lengthOfMonth());
        try {
            List<EntityExpr> exprsMain = UtilMisc.toList(EntityCondition.makeCondition("TransDate", EntityOperator.GREATER_THAN_EQUAL_TO, java.sql.Date.valueOf(startMonth)),
                    EntityCondition.makeCondition("TransDate", EntityOperator.LESS_THAN_EQUAL_TO, java.sql.Date.valueOf(endMonth)),
                    EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId),
                    EntityCondition.makeCondition("TransType", EntityOperator.EQUALS, transType));
            EntityCondition conditionMain = EntityCondition.makeCondition(exprsMain, EntityOperator.AND);
            System.out.println("conditionMain : " + conditionMain);
            List<GenericValue> result = delegator.findList("EmplSalaryGrade", conditionMain, null, null, null, true);
            System.out.println("transDate " + transDate);
            System.out.println("result from Trans Type" + result);
            if (!result.isEmpty()) {
                isExist = true;

            }

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return isExist;
    }

    public static String getAcctgTransCode_By_acctgTransId(HttpServletRequest request, HttpServletResponse response, String acctgTransId) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("acctgTransId", acctgTransId);
        String acctgTransCode = "";
        try {
            List<GenericValue> result = delegator.findList("AcctgTrans", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                acctgTransCode = result.get(0).get("acctgTransCode").toString();

            }
        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return acctgTransCode;
    }

    public static String getAcctTrans_FromEmplSalaryAcctgTrans(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        PrintWriter out = null;
        Map<String, Object> criteria = new HashMap<String, Object>();
        long year = Long.valueOf(request.getParameter("year"));
        long month = Long.valueOf(request.getParameter("month"));
        String jobGroup = request.getParameter("jobGroup").toString();
        criteria.put("year", year);
        criteria.put("month", month);
        criteria.put("jobGroupId", jobGroup);

        JSONObject jsonRes = new JSONObject();
        try {
            List<GenericValue> result = delegator.findList("EmplSalaryAcctgTrans", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (!result.isEmpty()) {
                jsonRes.put("acctgTransId", result.get(0).get("acctgTransId").toString());
                jsonRes.put("acctgTransCode", result.get(0).get("acctgTransCode").toString());
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(jsonRes.toString());
            out.flush();

        } catch (GenericEntityException ex) {
            Logger.getLogger(PayrollServices.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static ArrayList<Employee> getSalaryCalculation_ForEachEmployee(HttpServletRequest request, String partyId, int calculationMonthValue, int calculationYearValue) throws SQLException, ParseException, IOException {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String jobGroupId = "";
        int numOfDays = 0;
        ArrayList<Employee> employeeList = new ArrayList<Employee>();
        Employee employee = null;
        BigDecimal amount = new BigDecimal(0);
        BigDecimal roundAmount = new BigDecimal(0);
        int numberOfDecimal = getDecimalFormat_BasicSalary(request);
        String glAccountId = "";
        double totalAmount = 0.0;

        LocalDate startCalculationMonth = LocalDate.of(calculationYearValue, calculationMonthValue, 1);

//        to get Calculation Way either per 30 day or length of month
        String calculationWay = getGlobalPayrollSetting(request, "SALARY_CALCULATION_WAY");
        System.out.println("calculationWay " + calculationWay);
        Map<String, String> messageMap;
//        VALIDATION: check if the user select calculation way or not 
        if (calculationWay.equals("")) {

            return null;

        }
        int lengthOfMonthForCalculation = 0;
        int lengthOfMonth = startCalculationMonth.lengthOfMonth();
        if (calculationWay.equals("LENGTH_OF_MONTH")) {
            lengthOfMonthForCalculation = startCalculationMonth.lengthOfMonth();
        } else if (calculationWay.equals("THIRTY_DAYS")) {
            lengthOfMonthForCalculation = 30;
        }

        LocalDate endCalculationMonth = LocalDate.of(calculationYearValue, calculationMonthValue, lengthOfMonth);
        GenericValue gvValue = delegator.makeValue("EmplSalary");
        java.sql.Date localEndDate = null;
        Map<String, Object> BasicSalaryDetials;
        Map<String, Object> EmplDetials;
        Map<String, String> criteria = new HashMap<String, String>();

        java.sql.Date lastTransDate = null;
        java.sql.Date hiringDate = null;
        Map<String, Object> criteriaMap = new HashMap<String, Object>();
        criteriaMap.put("year", new Long(calculationYearValue));
        criteriaMap.put("month", new Long(calculationMonthValue));

        String emplStatus = "";
        boolean isTerminated_endMonth = false;
        boolean isTerminated_startMonth = false;
        boolean isHold_startDate = false;

        try {

//             VALIDATION: check if this month is posted 
//            if (isPosted_Totaly(request, new Long(calculationYearValue), new Long(calculationMonthValue))) {
//                return null;
//            }
            // get all employee HIRED from Person Entity            
//get LastTrans Type for employee  to check if the employee terminated or hold  or no ex :
// want to calculate month 10 and lastTrans Type in month 9 was be TERMINATED 
//so this service return TERMINATED Type so the employee dosen't have Salary in month 10 chekc from 1/10 to 31/10
            emplStatus = PersonUtilServices.isTerminated_isHOLD_Employee_ByMonth(request, partyId, startCalculationMonth, endCalculationMonth);

            isHold_startDate = PersonUtilServices.isExsistTransType_ByTransDate(request, partyId, java.sql.Date.valueOf(startCalculationMonth), "HOLD");

            if ((!emplStatus.equals("HOLD") && !emplStatus.equals("TERMINATED")) && !isHold_startDate) {

// get lastActive Transaction Date from EmplSalaryGrade Entity for each employee
                lastTransDate = getLastActive_TransDate_Employee_SalaryCalculation(request, partyId, calculationMonthValue, calculationYearValue);
                hiringDate = getHiredDate(request, partyId);
                if (lastTransDate != null && hiringDate != null) {
                    // get map from service below that contains  degreeId GradeId jobgroupId employment and BasicSalary
                    BasicSalaryDetials = getDataFromEmployeeSalaryGrade_SalaryCalculation(request, partyId, calculationMonthValue, calculationYearValue);
//                get Number of working days for each employee in this month based on lastTransDate
//          if emplStatus is Terminated in started month ex 31/1/2017 status Terminated
                    isTerminated_endMonth = PersonUtilServices.isExsistTransType_ByTransDate(request, partyId, java.sql.Date.valueOf(endCalculationMonth), "TERMINATED");

//          if emplStatus is Terminated in started month ex 01/1/2017 status Terminated so noOfDay =1
                    isTerminated_startMonth = PersonUtilServices.isExsistTransType_ByTransDate(request, partyId, java.sql.Date.valueOf(startCalculationMonth), "TERMINATED");

                    if (isTerminated_startMonth) {
                        numOfDays = 1;
                    } else {
                        numOfDays = getNoOFDays_SalaryCalculation(lastTransDate, null, startCalculationMonth, endCalculationMonth);
                    }

                    if (numOfDays == lengthOfMonth) {
                        amount = (BigDecimal) BasicSalaryDetials.get("basicSalary");
                        totalAmount = amount.doubleValue();
                        glAccountId = getGlAccountId(request, (String) getAlloweceIdForBasicSalary(request), "Emp");

                        employee = new Employee();
                        employee.setAmount(totalAmount);
                        employee.setType("BASIC_SALARY");
                        employee.setAmountType("الراتب الأساسي");
                        employee.setDegreeId((String) BasicSalaryDetials.get("degreeId"));
                        employee.setGradeId((String) BasicSalaryDetials.get("gradeId"));
                        employee.setJobGroupId((String) BasicSalaryDetials.get("jobGroupId"));
                        employee.setOrganizationPartyId((String) BasicSalaryDetials.get("organizationPartyId"));
                        employee.setAllowenceId((String) getAlloweceIdForBasicSalary(request));
                        employee.setGlAccountId(glAccountId);
                        employee.setPositionId((String) BasicSalaryDetials.get("positionId"));
                        employee.setMonth(new Long(calculationMonthValue));
                        employee.setYear(new Long(calculationYearValue));
                        employee.setNomDay(new Long(30));
                        employee.setIsCompany('N');
                        employee.setRelatedType("");
                        employee.setIsPosted('N');
                        employeeList.add(employee);

                    } else if (lengthOfMonth != numOfDays) {
                        List<Map> salaryDetails = getPreviousBasicSalary_SalaryCalculation(request, partyId, lengthOfMonthForCalculation, numberOfDecimal, startCalculationMonth, endCalculationMonth);
                        for (int i = 0; i < salaryDetails.size(); i++) {

                            glAccountId = getGlAccountId(request, (String) getAlloweceIdForBasicSalary(request), "Emp");

                            employee = new Employee();
                            employee.setAmount((double) salaryDetails.get(i).get("basicSalary"));
                            employee.setType("BASIC_SALARY");
                            employee.setAmountType("الراتب الأساسي");
                            employee.setDegreeId((String) salaryDetails.get(i).get("degreeId"));
                            employee.setGradeId((String) salaryDetails.get(i).get("degreeId"));
                            employee.setJobGroupId((String) salaryDetails.get(i).get("jobGroupId"));
                            employee.setOrganizationPartyId((String) salaryDetails.get(i).get("partyIdFrom"));
                            employee.setAllowenceId((String) getAlloweceIdForBasicSalary(request));
                            employee.setGlAccountId(glAccountId);
                            employee.setPositionId((String) salaryDetails.get(i).get("positionId"));
                            employee.setMonth(new Long(calculationMonthValue));
                            employee.setYear(new Long(calculationYearValue));
                            employee.setNomDay((Long) salaryDetails.get(i).get("numOfDays"));
                            employee.setIsCompany('N');
                            employee.setRelatedType("");
                            employee.setIsPosted('N');
                            employeeList.add(employee);
                        }
                    }

                }

            }

            //            for Allowence Grad 
            java.sql.Date allowenceStartDate = null;
            java.sql.Date allowenceEndDate = null;
            String factorId = "";

            emplStatus = PersonUtilServices.isTerminated_isHOLD_Employee_ByMonth(request, partyId, startCalculationMonth, endCalculationMonth);
            System.out.println("emplStatus from AllowenceGrade " + emplStatus + " " + partyId);
            isHold_startDate = PersonUtilServices.isExsistTransType_ByTransDate(request, partyId, java.sql.Date.valueOf(startCalculationMonth), "HOLD");

            if ((!emplStatus.equals("HOLD") && !emplStatus.equals("TERMINATED")) && !isHold_startDate) {

                // get lastActive Transaction Date from EmplSalaryGrade Entity for each employee
                lastTransDate = getLastActive_TransDate_Employee_SalaryCalculation(request, partyId, calculationMonthValue, calculationYearValue);
                hiringDate = getHiredDate(request, partyId);
                System.out.println("hiringDate from Allowences " + hiringDate);
                if (lastTransDate != null && hiringDate != null) {

                    // get map from service below that contains  jobGroupId GradeId And Department 
                    EmplDetials = getDataFromEmployeeSalaryGrade_SalaryCalculation(request, partyId, calculationMonthValue, calculationYearValue);
                    //get Number of working days for each employee in this month based on lastTransDate
//                   numOfDays = getNoOFDays_SalaryCalculation(lastTransDate, (java.sql.Date) null, calculationMonthValue, calculationYearValue);
                    isTerminated_endMonth = PersonUtilServices.isExsistTransType_ByTransDate(request, partyId, java.sql.Date.valueOf(endCalculationMonth), "TERMINATED");

//          if emplStatus is Terminated in started month ex 01/1/2017 status Terminated so noOfDay =1
                    isTerminated_startMonth = PersonUtilServices.isExsistTransType_ByTransDate(request, partyId, java.sql.Date.valueOf(startCalculationMonth), "TERMINATED");

                    if (isTerminated_startMonth) {
                        numOfDays = 1;
                    } else {
                        numOfDays = getNoOFDays_SalaryCalculation(lastTransDate, null, startCalculationMonth, endCalculationMonth);
                    }
                    List<Map> AllownceGradeList = getAllowenceGradeData_SalaryCalculation(request, (String) EmplDetials.get("gradeId"));
                    if (numOfDays == lengthOfMonth) {
                        for (int i = 0; i < AllownceGradeList.size(); i++) {
                            allowenceStartDate = (java.sql.Date) AllownceGradeList.get(i).get("startDate");
                            allowenceEndDate = (java.sql.Date) AllownceGradeList.get(i).get("endDate");

                            if (allowenceEndDate == null || endCalculationMonth.isBefore(allowenceEndDate.toLocalDate()) || endCalculationMonth.isEqual(allowenceEndDate.toLocalDate())) {
                                int numOfDaysGrade = getNoOFDays_SalaryCalculation(allowenceStartDate, (java.sql.Date) null, startCalculationMonth, endCalculationMonth);
                                if (allowenceStartDate.toLocalDate().isBefore(startCalculationMonth) || allowenceStartDate.toLocalDate().isEqual(startCalculationMonth)
                                        || allowenceStartDate.toLocalDate().isBefore(endCalculationMonth) || allowenceStartDate.toLocalDate().isEqual(endCalculationMonth)) {
                                    employee = new Employee();
                                    glAccountId = getGlAccountId(request, (String) AllownceGradeList.get(i).get("allowenceId"), "Emp");
                                    factorId = getAllowenceFactorId(request, (String) AllownceGradeList.get(i).get("allowenceId"));
                                    if (factorId.equals("SAL_INCREMENT")) {
                                        amount = ((BigDecimal) AllownceGradeList.get(i).get("amount"));
                                    } else {
                                        amount = ((BigDecimal) AllownceGradeList.get(i).get("amount")).multiply(new BigDecimal(-1));
                                    }
                                    if (numOfDaysGrade == lengthOfMonth) {

                                        roundAmount = new BigDecimal(amount.doubleValue());
                                        roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);
                                        employee.setAmount(roundAmount.doubleValue());
                                        employee.setNomDay(new Long(30));

                                    } else if (numOfDaysGrade != lengthOfMonth) {
                                        totalAmount = (amount.doubleValue() / lengthOfMonthForCalculation) * numOfDaysGrade;
                                        roundAmount = new BigDecimal(totalAmount);
                                        roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);
                                        employee.setAmount(roundAmount.doubleValue());
                                        employee.setNomDay(new Long(30));
                                    }

                                    employee.setType("ALLOWENCE_GRADE");
                                    employee.setAmountType("بدل المرتبة");
                                    employee.setDegreeId("");
                                    employee.setGradeId((String) EmplDetials.get("gradeId"));
                                    employee.setJobGroupId((String) EmplDetials.get("jobGroupId"));
                                    employee.setOrganizationPartyId((String) EmplDetials.get("organizationPartyId"));
                                    employee.setAllowenceId((String) AllownceGradeList.get(i).get("allowenceId"));
                                    employee.setGlAccountId(glAccountId);
                                    employee.setPositionId("-");
                                    employee.setMonth(new Long(calculationMonthValue));
                                    employee.setYear(new Long(calculationYearValue));
                                    employee.setRelatedType("");
                                    employee.setIsCompany('N');
                                    employee.setIsPosted('N');
                                    employeeList.add(employee);
                                }
                            }

                        }

                    } else if (isTerminated_startMonth) {
                        for (int i = 0; i < AllownceGradeList.size(); i++) {
                            allowenceStartDate = (java.sql.Date) AllownceGradeList.get(i).get("startDate");
                            allowenceEndDate = (java.sql.Date) AllownceGradeList.get(i).get("endDate");

                            if (allowenceEndDate == null || endCalculationMonth.isBefore(allowenceEndDate.toLocalDate()) || endCalculationMonth.isEqual(allowenceEndDate.toLocalDate())) {
///                                        int numOfDaysGrade = getNoOFDays_SalaryCalculation(allowenceStartDate, (java.sql.Date) null, calculationMonthValue, calculationYearValue);
                                if (allowenceStartDate.toLocalDate().isBefore(startCalculationMonth) || allowenceStartDate.toLocalDate().isEqual(startCalculationMonth)
                                        || allowenceStartDate.toLocalDate().isBefore(endCalculationMonth) || allowenceStartDate.toLocalDate().isEqual(endCalculationMonth)) {
                                    employee = new Employee();

                                    factorId = getAllowenceFactorId(request, (String) AllownceGradeList.get(i).get("allowenceId"));
                                    if (factorId.equals("SAL_INCREMENT")) {
                                        amount = ((BigDecimal) AllownceGradeList.get(i).get("amount"));
                                    } else {
                                        amount = ((BigDecimal) AllownceGradeList.get(i).get("amount")).multiply(new BigDecimal(-1));
                                    }
//                                                                            totalAmount = (amount.doubleValue() / lengthOfMonthForCalculation);
                                    roundAmount = new BigDecimal(totalAmount);
                                    roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);
                                    employee.setAmount(roundAmount.doubleValue());
                                    employee.setNomDay(new Long(1));
                                    employee.setType("ALLOWENCE_GRADE");
                                    employee.setAmountType("بدل المرتبة");
                                    employee.setDegreeId("");
                                    employee.setGradeId((String) EmplDetials.get("gradeId"));
                                    employee.setJobGroupId((String) EmplDetials.get("jobGroupId"));
                                    employee.setOrganizationPartyId((String) EmplDetials.get("organizationPartyId"));
                                    employee.setAllowenceId((String) AllownceGradeList.get(i).get("allowenceId"));
                                    employee.setGlAccountId(glAccountId);
                                    employee.setPositionId("-");
                                    employee.setMonth(new Long(calculationMonthValue));
                                    employee.setYear(new Long(calculationYearValue));
                                    employee.setIsCompany('N');
                                    employee.setIsPosted('N');
                                    employee.setRelatedType("");
                                    employeeList.add(employee);

                                }
                            }

                        }

                    } else if (numOfDays != lengthOfMonth) {
                        System.out.println("I'm here " + partyId);
                        List<List<Map>> AllowenceGradeListPrev = getPreviousAllowenceGrade_SalaryCalculation(request, partyId, lengthOfMonthForCalculation, numberOfDecimal, startCalculationMonth, endCalculationMonth);
                        System.out.println("AllowenceGradeListPrev " + AllowenceGradeListPrev);
                        System.out.println("AllowenceGradeListPrev Size " + AllowenceGradeListPrev.size());
                        for (int i = 0; i < AllowenceGradeListPrev.size(); i++) {
                            for (int j = 0; j < AllowenceGradeListPrev.get(i).size(); j++) {
                                allowenceStartDate = (java.sql.Date) AllowenceGradeListPrev.get(i).get(j).get("startDate");
                                allowenceEndDate = (java.sql.Date) AllowenceGradeListPrev.get(i).get(j).get("endDate");
                                if (allowenceEndDate == null || endCalculationMonth.isBefore(allowenceEndDate.toLocalDate()) || endCalculationMonth.isEqual(allowenceEndDate.toLocalDate())) {
                                    if (allowenceStartDate.toLocalDate().isBefore(startCalculationMonth) || allowenceStartDate.toLocalDate().isEqual(startCalculationMonth) || allowenceStartDate.toLocalDate().isBefore(endCalculationMonth)) {

                                        glAccountId = getGlAccountId(request, (String) AllowenceGradeListPrev.get(i).get(j).get("allowenceId"), "Emp");

                                        roundAmount = new BigDecimal(totalAmount);
                                        roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);
                                        employee = new Employee();
                                        employee.setAmount((double) AllowenceGradeListPrev.get(i).get(j).get("amount"));
                                        employee.setNomDay((Long) AllowenceGradeListPrev.get(i).get(j).get("numOfDays"));
                                        employee.setType("ALLOWENCE_GRADE");
                                        employee.setAmountType("بدل المرتبة");
                                        employee.setDegreeId("");
                                        employee.setGradeId((String) AllowenceGradeListPrev.get(i).get(j).get("gradeId"));
                                        employee.setJobGroupId((String) EmplDetials.get("jobGroupId"));
                                        employee.setOrganizationPartyId((String) EmplDetials.get("organizationPartyId"));
                                        employee.setAllowenceId((String) AllowenceGradeListPrev.get(i).get(j).get("allowenceId"));
                                        employee.setGlAccountId(glAccountId);
                                        employee.setPositionId("-");
                                        employee.setMonth(new Long(calculationMonthValue));
                                        employee.setYear(new Long(calculationYearValue));
                                        employee.setIsCompany('N');
                                        employee.setIsPosted('N');
                                        employee.setRelatedType("");
                                        employeeList.add(employee);
                                    }
                                }
                            }

                        }
                    }

                }

            }

//            for Allowence Employee 
            String allowenceRelated = "";
            Map<String, Object> criteriaAllowences = new HashMap<String, Object>();
            criteriaAllowences.put("partyId", partyId);
            String acctgTransCode = "";

            List<GenericValue> allownceEmployeeList = delegator.findList("EmpAllowencesView", EntityCondition.makeCondition(criteriaAllowences), null, null, null, true);

            for (GenericValue genericValue : allownceEmployeeList) {

                emplStatus = PersonUtilServices.isTerminated_isHOLD_Employee_ByMonth(request, partyId, startCalculationMonth, endCalculationMonth);
                isHold_startDate = PersonUtilServices.isExsistTransType_ByTransDate(request, partyId, java.sql.Date.valueOf(startCalculationMonth), "HOLD");

                if ((!emplStatus.equals("HOLD") && !emplStatus.equals("TERMINATED")) && !isHold_startDate) {

                    isTerminated_endMonth = PersonUtilServices.isExsistTransType_ByTransDate(request, partyId, java.sql.Date.valueOf(endCalculationMonth), "TERMINATED");

//          if emplStatus is Terminated in started month ex 01/1/2017 status Terminated so noOfDay =1
                    isTerminated_startMonth = PersonUtilServices.isExsistTransType_ByTransDate(request, partyId, java.sql.Date.valueOf(startCalculationMonth), "TERMINATED");

                    jobGroupId = getJobGroupId(request, partyId);

                    hiringDate = getHiredDate(request, partyId);

                    java.sql.Date AllowenceStartDate = (java.sql.Date) genericValue.get("startDate");
                    System.out.println("Allowence Start " + AllowenceStartDate);
                    if (AllowenceStartDate.toLocalDate().isEqual(hiringDate.toLocalDate()) || AllowenceStartDate.toLocalDate().isAfter(hiringDate.toLocalDate())) {

                        if (genericValue.get("endDate") != null) {
                            localEndDate = (java.sql.Date) genericValue.get("endDate");
                        } else {
                            localEndDate = null;
                        }
                        if (genericValue.get("endDate") == null || endCalculationMonth.isBefore(localEndDate.toLocalDate()) || endCalculationMonth.isEqual(localEndDate.toLocalDate())) {
                            if (endCalculationMonth.isAfter(AllowenceStartDate.toLocalDate()) || endCalculationMonth.isEqual(AllowenceStartDate.toLocalDate())) {
                                if (genericValue.get("endDate") == null) {
                                    numOfDays = getNoOFDays_SalaryCalculation((java.sql.Date) genericValue.get("startDate"), (java.sql.Date) null, startCalculationMonth, endCalculationMonth);
                                } else if (genericValue.get("endDate") != null) {
                                    numOfDays = getNoOFDays_SalaryCalculation((java.sql.Date) genericValue.get("startDate"), (java.sql.Date) genericValue.get("endDate"), startCalculationMonth, endCalculationMonth);
                                }
                                if (genericValue.get("factorId").equals("SAL_INCREMENT")) {
                                    amount = ((BigDecimal) genericValue.get("amount"));
                                } else if (genericValue.get("factorId").equals("SAL_DEDUCT")) {
                                    amount = ((BigDecimal) genericValue.get("amount")).multiply(new BigDecimal(-1));
                                }
                                if (numOfDays != lengthOfMonth) {
                                    totalAmount = (amount.doubleValue() / lengthOfMonthForCalculation) * numOfDays;
                                } else if (numOfDays == lengthOfMonth) {
                                    numOfDays = 30;
                                    totalAmount = amount.doubleValue();
                                }
                                roundAmount = new BigDecimal(totalAmount);
                                roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);

                                allowenceRelated = (String) genericValue.get("typeId");
                                System.out.println("allowenceRelated " + allowenceRelated);
                                if (allowenceRelated.equals("COM")) {
                                    employee.setAmountType("بدل على الشركة");
                                    employee.setIsCompany('Y');
                                    glAccountId = getGlAccountId(request, (String) genericValue.get("allowenceId"), "Comp");

                                } else if (allowenceRelated.equals("EMP")) {
                                    employee.setAmountType("بدل الموظف");
                                    employee.setIsCompany('N');
                                    glAccountId = getGlAccountId(request, (String) genericValue.get("allowenceId"), "Emp");
                                }

                                employee = new Employee();
                                employee.setAmount(roundAmount.doubleValue());
                                employee.setNomDay(new Long(numOfDays));
                                employee.setType("ALLOWENCE_EMPLOYEE");
                                employee.setAmountType("بدل الموظف");
                                employee.setDegreeId("");
                                employee.setGradeId("");
                                employee.setJobGroupId(jobGroupId);
                                employee.setOrganizationPartyId((String) getDepartmentId(request, (String) genericValue.get("partyId"), calculationMonthValue, calculationYearValue));
                                employee.setAllowenceId((String) genericValue.get("allowenceId"));
                                employee.setGlAccountId(glAccountId);
                                employee.setPositionId("-");
                                employee.setMonth(new Long(calculationMonthValue));
                                employee.setYear(new Long(calculationYearValue));
                                employee.setRelatedType(allowenceRelated);
                                employee.setIsPosted('N');
                                employeeList.add(employee);
                            }
                        } else if (endCalculationMonth.isAfter(localEndDate.toLocalDate())) {
                            int currentMonth = endCalculationMonth.getMonthValue();
                            int currentYear = endCalculationMonth.getYear();
                            int endMonth = localEndDate.toLocalDate().getMonthValue();
                            int endYear = localEndDate.toLocalDate().getYear();
                            if (endYear == currentYear && endMonth == currentMonth) {

                                glAccountId = getGlAccountId(request, (String) genericValue.get("allowenceId"), "Emp");
                                allowenceRelated = (String) genericValue.get("typeId");
                                System.out.println("allowenceRelated " + allowenceRelated);
                                if (allowenceRelated.equals("COM")) {
                                    employee.setAmountType("بدل على الشركة");
                                    employee.setIsCompany('Y');

                                    glAccountId = getGlAccountId(request, (String) genericValue.get("allowenceId"), "Comp");
                                } else if (allowenceRelated.equals("EMP")) {
                                    employee.setAmountType("بدل الموظف");
                                    employee.setIsCompany('N');
                                    glAccountId = getGlAccountId(request, (String) genericValue.get("allowenceId"), "Emp");
                                }
                                numOfDays = getNoOFDays_SalaryCalculation((java.sql.Date) genericValue.get("startDate"), (java.sql.Date) genericValue.get("endDate"), startCalculationMonth, endCalculationMonth);
                                if (genericValue.get("factorId").equals("SAL_INCREMENT")) {
                                    amount = ((BigDecimal) genericValue.get("amount"));
                                } else {
                                    amount = ((BigDecimal) genericValue.get("amount")).multiply(new BigDecimal(-1));
                                }
                                if (numOfDays != lengthOfMonth) {
                                    totalAmount = (amount.doubleValue() / lengthOfMonthForCalculation) * numOfDays;
                                } else {
                                    numOfDays = 30;
                                    totalAmount = amount.doubleValue();
                                }
                                roundAmount = new BigDecimal(totalAmount);
                                roundAmount = roundAmount.setScale(numberOfDecimal, RoundingMode.HALF_UP);

                                employee = new Employee();
                                employee.setRelatedType(allowenceRelated);
                                employee.setAmount(roundAmount.doubleValue());
                                employee.setNomDay(new Long(numOfDays));
                                employee.setType("ALLOWENCE_EMPLOYEE");

                                employee.setDegreeId("");
                                employee.setGradeId("");
                                employee.setJobGroupId(jobGroupId);
                                employee.setOrganizationPartyId((String) getDepartmentId(request, (String) genericValue.get("partyId"), calculationMonthValue, calculationYearValue));
                                employee.setAllowenceId((String) genericValue.get("allowenceId"));
                                employee.setGlAccountId(glAccountId);
                                employee.setPositionId("-");
                                employee.setMonth(new Long(calculationMonthValue));
                                employee.setYear(new Long(calculationYearValue));

                                employee.setIsPosted('N');
                                employeeList.add(employee);
                            }
                        }
                    }

                }
            }
//              
        } catch (Exception ex) {

            Logger.getLogger(PayrollServices.class.getName()).log(Level.SEVERE, null, ex);
            return null;

        }

        return employeeList;

    }

    public static String SalaryCalculation_EndOfService(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        Map<String, String> messageMap;
        messageMap = UtilMisc.toMap("successMessage", "calculatedSuccessFully");

        String partyId = request.getParameter("partyId");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String expenseId = request.getParameter("expenseId");

        Map<String, Object> criteriaMap = new HashMap<String, Object>();
        criteriaMap.put("partyId", partyId);
        criteriaMap.put("year", new Long(year));
        criteriaMap.put("month", new Long(month));
        criteriaMap.put("expenseId", expenseId);
        criteriaMap.put("expenseType", "END_OF_SERVICE");
        criteriaMap.put("paymentTypeId", "PAY_TYPE_SEPARATED");

        if (expenseId.isEmpty()) {
            System.out.println("** expenseId.isEmpty() ** " + expenseId);
            messageMap = UtilMisc.toMap("errorMessage", "Expense Id Not Found");
            String errMsg = UtilProperties.getMessage(resourceError, "ExpenseIdNoFound", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }
        if (isPosted_Totaly(request, partyId, new Long(year), new Long(month), expenseId, "END_OF_SERVICE")) {
            messageMap = UtilMisc.toMap("errorMessage", "Posted");
            String errMsg = UtilProperties.getMessage(resourceError, "Posted", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }
        System.out.println("** expenseId " + expenseId);
        List<GenericValue> toBeStored = new LinkedList<GenericValue>();
        int c = 0;
        try {
            ArrayList<Employee> salaryCalculationData = getSalaryCalculation_ForEachEmployee(request, partyId, Integer.valueOf(month), Integer.valueOf(year));
            System.out.println("** salaryCalculationData **");
            System.out.println(salaryCalculationData);

//            if (salaryCalculationData == null) {
//                messageMap = UtilMisc.toMap("errorMessage", "Posted");
//                String errMsg = UtilProperties.getMessage(resourceError, "Posted", messageMap, UtilHttp.getLocale(request));
//                request.setAttribute("_ERROR_MESSAGE_", errMsg);
//                return "error";
//            }
            if (!salaryCalculationData.isEmpty()) {
                System.out.println(salaryCalculationData);
                System.out.println(salaryCalculationData);

                int removePartySalary = delegator.removeByAnd("EmplSalary", criteriaMap);

                GenericValue gvValue = delegator.makeValue("EmplSalary");
                for (Employee row : salaryCalculationData) {
                    c++;
                    System.out.println("getAllowenceId ** " + row.getAllowenceId());
                    System.out.println("getIsCompany ** " + row.getIsCompany());

                    gvValue.set("emplSalaryId", delegator.getNextSeqId("emplSalaryId"));
                    System.out.println("** emplSalaryId **" + gvValue.get("emplSalaryId"));
                    gvValue.set("partyId", partyId);
                    if (row.getGradeId().isEmpty()) {
                        gvValue.set("gradeId", "-");
                    } else {
                        gvValue.set("gradeId", row.getGradeId());
                    }
                    if (row.getDegreeId().isEmpty()) {
                        gvValue.set("degreeId", "-");
                    } else {
                        gvValue.set("degreeId", row.getDegreeId());
                    }
                    gvValue.set("jobGroupId", row.getJobGroupId());
                    gvValue.set("organizationPartyId", row.getOrganizationPartyId());
                    gvValue.set("allowenceId", row.getAllowenceId());
                    gvValue.set("amountType", row.getAmountType());
                    gvValue.set("positionId", row.getPositionId());
                    gvValue.set("glAccountId", row.getGlAccountId());
                    gvValue.set("month", new Long(month));
                    gvValue.set("year", new Long(year));
                    gvValue.set("amount", row.getAmount());
                    gvValue.set("nomDay", row.getNomDay());
                    gvValue.set("userLoginId", getUserLoginId(request));
                    gvValue.set("isCompany", String.valueOf(row.getIsCompany()));//
                    gvValue.set("isPosted", String.valueOf(row.getIsPosted()));//
                    gvValue.set("expenseId", expenseId);
                    gvValue.set("expenseType", "END_OF_SERVICE");
                    gvValue.set("paymentTypeId", "PAY_TYPE_SEPARATED");

                    try {
                        System.out.println("** *" + c + "* **" + gvValue);
                        System.out.println(gvValue);
                        gvValue.create();
                    } catch (Exception e) {
                        e.getMessage();
                        e.printStackTrace();
                        messageMap = UtilMisc.toMap("errorMessage", e.getMessage());
                        String errMsg = UtilProperties.getMessage(resourceError, e.getMessage(), messageMap, UtilHttp.getLocale(request));
                        request.setAttribute("_ERROR_MESSAGE_", errMsg);
                        return "error";
                    }
                }
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println();
            out.flush();
        } catch (Exception ex) {
            ex.getMessage();
            ex.printStackTrace();
            messageMap = UtilMisc.toMap("errorMessage", ex.getMessage());
            String errMsg = UtilProperties.getMessage(resourceError, ex.getMessage(), messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }
        String successMsg = UtilProperties.getMessage(resourceError, "calculatedSuccessFully", messageMap, UtilHttp.getLocale(request));
        request.setAttribute("_EVENT_MESSAGE_", successMsg);
        return "success";
    }

    public static String addAllowEndofService_Old(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        Map<String, String> messageMap;
        messageMap = UtilMisc.toMap("successMessage", "addSuccessFully");

        String partyId = request.getParameter("partyId");
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String expenseId = request.getParameter("expenseId");
        String allowAmount = request.getParameter("allowAmount");
        String allowencesId = request.getParameter("allowencesId");
        String expenseType = request.getParameter("expenseType");
        Object fileBase64Obj = request.getParameter("fileBase64");
        String fileBase64 = (String) fileBase64Obj;
        String fileExtension = request.getParameter("fileExtension");
        String tenantId = request.getParameter("userTenantId");

        System.out.println("fileBase64Obj ** " + fileBase64Obj);
        System.out.println("fileBase64 ** " + fileBase64);
        System.out.println("fileExtension ** " + fileExtension);
        System.out.println("tenantId ** " + tenantId);
//        tenantId = "1001";

        Random rand = new Random();
        String randomNumber = String.valueOf(rand.nextInt(50000 - 10000) + 10000);

        System.out.println("randomNumber  ** " + randomNumber);
        System.out.println("fileExtension ** " + fileExtension);

        String sourceFolder = "framework\\images\\webapp\\images\\Attachment\\";

        if (tenantId != null) {
            if (!tenantId.isEmpty()) {
                sourceFolder = sourceFolder.concat(tenantId + "\\");
            }
        }
//        File file = new File(sourceFolder);
//        if (!file.exists()) {
//            if (file.mkdirs()) {
//                System.out.println("Multiple directories are created!");
//            } else {
//                System.out.println("Failed to create multiple directories!");
//            }
//        }
        System.out.println("sourceFolder ** " + sourceFolder);
        fileBase64 = fileBase64.replaceAll(" ", "+");
        byte[] btDataFile = new sun.misc.BASE64Decoder().decodeBuffer(fileBase64);
        String Imagetest = expenseId + "_" + randomNumber + "." + fileExtension;

        Map<String, Object> criteriaMap = new HashMap<String, Object>();
        criteriaMap.put("partyId", partyId);
        criteriaMap.put("year", new Long(year));
        criteriaMap.put("month", new Long(month));
        criteriaMap.put("expenseId", expenseId);
        criteriaMap.put("expenseType", expenseType);
        criteriaMap.put("paymentTypeId", "PAY_TYPE_SEPARATED");
        criteriaMap.put("allowenceId", allowencesId);

        if (expenseId.isEmpty()) {
            System.out.println("** expenseId.isEmpty() ** " + expenseId);
            messageMap = UtilMisc.toMap("errorMessage", "Expense Id Not Found");
            String errMsg = UtilProperties.getMessage(resourceError, "ExpenseNotFound", messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }
        System.out.println("** expenseId " + expenseId);

        if (isPosted_Totaly(request, partyId, new Long(year), new Long(month), expenseId, expenseType, allowencesId)) {
            System.out.println("** isPosted_Totaly **");
            messageMap = UtilMisc.toMap("errorMessage", "Posted");
            String errMsg = UtilProperties.getMessage(resourceError, "AllowEndofServicePosted", UtilMisc.toMap("allowencesId", allowencesId), UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }
        String emplSalaryId = "";
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("allowenceId", allowencesId);
        try {
//            int removePartySalary = delegator.removeByAnd("EmplSalary", criteriaMap);

            GenericValue resultAlloW = delegator.findOne("Allowences", criteria, true);
            String allowenceRelated = resultAlloW.get("relatedToId").toString();
            String allowenceGlAccountIdEmp = resultAlloW.get("glAccountIdEmp").toString();
            String allowenceTypeId = (String) resultAlloW.get("allowenceTypeId");
            System.out.println("** allowenceRelated " + allowenceRelated);

            System.out.println("** addAllowEndofService **");
            Map<String, Object> emplDetials = getDataFromEmployeeSalaryGrade_SalaryCalculation(request, partyId, Integer.valueOf(month), Integer.valueOf(year));
            GenericValue gvValue = delegator.makeValue("EmplSalary");
            emplSalaryId = delegator.getNextSeqId("emplSalaryId");
            gvValue.set("emplSalaryId", emplSalaryId);
            gvValue.set("partyId", partyId);
            gvValue.set("gradeId", emplDetials.get("gradeId"));
            gvValue.set("degreeId", emplDetials.get("degreeId"));
            gvValue.set("jobGroupId", emplDetials.get("jobGroupId"));
            gvValue.set("organizationPartyId", emplDetials.get("organizationPartyId"));
            gvValue.set("allowenceId", allowencesId);
            if (allowenceRelated.equals("COM")) {
                gvValue.set("amountType", "بدل على الشركة");
            } else if (allowenceRelated.equals("EMP")) {
                gvValue.set("amountType", "بدل الموظف");
            }
            gvValue.set("positionId", emplDetials.get("positionId"));
            gvValue.set("glAccountId", allowenceGlAccountIdEmp);
            gvValue.set("month", new Long(month));
            gvValue.set("year", new Long(year));
            if (allowenceTypeId.equals("ADD")) {
                gvValue.set("amount", Double.valueOf(allowAmount));
            } else {
                gvValue.set("amount", Double.valueOf(allowAmount) * -1);
            }
//            gvValue.set("nomDay", row.getNomDay());
            gvValue.set("userLoginId", getUserLoginId(request));
            gvValue.set("isCompany", "N");//
            gvValue.set("isPosted", "N");//
            gvValue.set("expenseId", expenseId);
            gvValue.set("expenseType", expenseType);
            gvValue.set("paymentTypeId", "PAY_TYPE_SEPARATED");

            System.out.println("btDataFile " + btDataFile);
            System.out.println("Imagetest *url* " + sourceFolder + Imagetest);
            File of = new File(sourceFolder + Imagetest);
//            ByteArrayInputStream bais = new ByteArrayInputStream(of);
            FileOutputStream osf = new FileOutputStream(of);
            osf.write(btDataFile);
            osf.flush();

            try {
                System.out.println("** ** **");
                System.out.println(gvValue);
                gvValue.create();
            } catch (Exception e) {
                e.getMessage();
                messageMap = UtilMisc.toMap("errorMessage", e.getMessage());
                String errMsg = UtilProperties.getMessage(resourceError, e.getMessage(), messageMap, UtilHttp.getLocale(request));
                request.setAttribute("_ERROR_MESSAGE_", errMsg);
                return "error";
            }
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println();
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
            messageMap = UtilMisc.toMap("errorMessage", ex.getMessage());
            String errMsg = UtilProperties.getMessage(resourceError, ex.getMessage(), messageMap, UtilHttp.getLocale(request));
            request.setAttribute("_ERROR_MESSAGE_", errMsg);
            return "error";
        }
        String successMsg = UtilProperties.getMessage(resourceError, "addAllowEndofServiceSuccessFully", messageMap, UtilHttp.getLocale(request));
        request.setAttribute("_EVENT_MESSAGE_", successMsg);
        request.setAttribute("emplSalaryId", emplSalaryId);
        return "success";
    }

    public static Map<String, Object> addAllowEndofService(DispatchContext dctx, Map<String, Object> context) throws ParseException, IOException, SQLException, GenericEntityException {
        Locale locale = (Locale) context.get("locale");
        Delegator delegator = dctx.getDelegator();

        Map<String, String> messageMap;
        messageMap = UtilMisc.toMap("successMessage", "addSuccessFully");

        String year = (String) context.get("year");
        String month = (String) context.get("month");
        String allowencesId = (String) context.get("allowencesId");
        String allowAmount = (String) context.get("allowAmount");
        String partyId = (String) context.get("partyId");
        String expenseId = (String) context.get("expenseId");
        String expenseType = (String) context.get("expenseType");
        String fileExtension = (String) context.get("fileExtension");
        String tenantId = (String) delegator.getDelegatorTenantId();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String userLoginId = userLogin.getString("userLoginId");
        ByteBuffer fileBytes = (ByteBuffer) context.get("uploadedFile");

        System.out.println("fileBytes " + fileBytes.array().length);
        System.out.println("1 tenantId " + tenantId);//1001

        Map<String, Object> criteriaMap = new HashMap<String, Object>();
        criteriaMap.put("partyId", partyId);
        criteriaMap.put("year", new Long(year));
        criteriaMap.put("month", new Long(month));
        criteriaMap.put("expenseId", expenseId);
        criteriaMap.put("expenseType", expenseType);
        criteriaMap.put("paymentTypeId", "PAY_TYPE_SEPARATED");
        criteriaMap.put("allowenceId", allowencesId);

        if (expenseId.isEmpty()) {
            System.out.println("** expenseId.isEmpty() ** " + expenseId);
            messageMap = UtilMisc.toMap("errorMessage", "Expense Id Not Found");
            String errMsg = UtilProperties.getMessage(resourceError, "ExpenseNotFound", messageMap, locale);
            return ServiceUtil.returnError(errMsg);
        }

        String sourceFolder = "";
        String urlAttachment = "";
        String fileName = "";
        if (fileBytes.array().length > 0) {
            if (fileExtension == null || fileExtension.isEmpty()) {
                System.out.println("** fileExtension.isEmpty() ** " + expenseId);
                messageMap = UtilMisc.toMap("errorMessage", "File Extension Not Found");
                String errMsg = UtilProperties.getMessage(resourceError, "FileExtensionNotFound", messageMap, locale);
                return ServiceUtil.returnError(errMsg);
            }
            Random rand = new Random();
            String randomNumber = String.valueOf(rand.nextInt(50000 - 10000) + 10000);

            sourceFolder = "framework\\images\\webapp\\images\\Attachment\\";
            urlAttachment = "/images/Attachment/";
            if (tenantId != null) {
                if (!tenantId.isEmpty()) {
                    sourceFolder = sourceFolder.concat(tenantId + "\\");
                    urlAttachment = urlAttachment.concat(tenantId + "/");
                }
            }
            File fileExists = new File(sourceFolder);
            if (fileExists.exists()) {
                System.out.println("file exists ");
            } else {
                System.out.println("file not exists ");
                fileExists.mkdirs();
            }

            fileName = expenseId + "_" + randomNumber + "." + fileExtension;
            System.out.println("fileName *url* " + sourceFolder + fileName);
            urlAttachment = urlAttachment.concat(fileName);
            System.out.println("urlAttachment **" + urlAttachment);
        }// check if file not empty

        String emplSalaryId = "";
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("allowenceId", allowencesId);
        try {
            GenericValue resultAlloW = delegator.findOne("Allowences", criteria, true);
            String allowenceRelated = resultAlloW.get("relatedToId").toString();
            String allowenceGlAccountIdEmp = resultAlloW.get("glAccountIdEmp").toString();
            String allowenceTypeId = (String) resultAlloW.get("allowenceTypeId");

            System.out.println("** addAllowEndofService **");
            Map<String, Object> emplDetials = getDataFromEmployeeSalaryGrade_SalaryCalculation(delegator, partyId, Integer.valueOf(month), Integer.valueOf(year));
            GenericValue gvValue = delegator.makeValue("EmplSalary");
            emplSalaryId = delegator.getNextSeqId("emplSalaryId");
            gvValue.set("emplSalaryId", emplSalaryId);
            gvValue.set("partyId", partyId);
            gvValue.set("gradeId", emplDetials.get("gradeId"));
            gvValue.set("degreeId", emplDetials.get("degreeId"));
            gvValue.set("jobGroupId", emplDetials.get("jobGroupId"));
            gvValue.set("organizationPartyId", emplDetials.get("organizationPartyId"));
            gvValue.set("allowenceId", allowencesId);
            if (allowenceRelated.equals("COM")) {
                gvValue.set("amountType", "بدل على الشركة");
            } else if (allowenceRelated.equals("EMP")) {
                gvValue.set("amountType", "بدل الموظف");
            }
            gvValue.set("positionId", emplDetials.get("positionId"));
            gvValue.set("glAccountId", allowenceGlAccountIdEmp);
            gvValue.set("month", new Long(month));
            gvValue.set("year", new Long(year));
            if (allowenceTypeId.equals("ADD")) {
                gvValue.set("amount", Double.valueOf(allowAmount));
            } else {
                gvValue.set("amount", Double.valueOf(allowAmount) * -1);
            }
//            gvValue.set("nomDay", row.getNomDay());
            gvValue.set("userLoginId", userLoginId);//getUserLoginId(request)
            gvValue.set("isCompany", "N");//
            gvValue.set("isPosted", "N");//
            gvValue.set("expenseId", expenseId);
            gvValue.set("expenseType", expenseType);
            gvValue.set("paymentTypeId", "PAY_TYPE_SEPARATED");
            gvValue.set("urlAttachment", urlAttachment);

            if (fileBytes.array().length > 0) {
//          ByteArrayInputStream bais = new ByteArrayInputStream(fileBytes.array());
                byte[] btDataFile = fileBytes.array();
                try {
                    File of = new File(sourceFolder + fileName);
                    FileOutputStream osf = new FileOutputStream(of);
                    osf.write(btDataFile);
                    osf.flush();
                } catch (Exception ex) {
                    ex.getMessage();
                    ex.printStackTrace();
                    messageMap = UtilMisc.toMap("errorMessage", ex.getMessage());
                    String errMsg = UtilProperties.getMessage(resourceError, ex.getMessage(), messageMap, locale);
                    return ServiceUtil.returnError(errMsg);
                }
            }// check if file not empty
            try {
                System.out.println("** ** **");
                System.out.println(gvValue);
                gvValue.create();

            } catch (Exception ex) {
                ex.getMessage();
                ex.printStackTrace();
                messageMap = UtilMisc.toMap("errorMessage", ex.getMessage());
                String errMsg = UtilProperties.getMessage(resourceError, ex.getMessage(), messageMap, locale);
                return ServiceUtil.returnError(errMsg);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            messageMap = UtilMisc.toMap("errorMessage", ex.getMessage());
            String errMsg = UtilProperties.getMessage(resourceError, ex.getMessage(), messageMap, locale);
            return ServiceUtil.returnError(errMsg);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        messageMap = UtilMisc.toMap("successMessage", "addSuccessFully");
        String successMsg = UtilProperties.getMessage(resourceError, "addAllowEndofServiceSuccessFully", messageMap, locale);

        return ServiceUtil.returnSuccess(successMsg);
    }

    public static String getNewExpenseId(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        String expenseId = delegator.getNextSeqId("EXPENSE_ID");///make-next-seq-id
        HttpSession session = request.getSession();
        session.setAttribute("expenseId", expenseId);
        System.out.println("** expenseId " + expenseId);

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        out = response.getWriter();
        out.println(expenseId);
        out.flush();

        return "";
    }

    public static String setExpenseId(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
//        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        String expenseId = request.getParameter("expenseId");
        HttpSession session = request.getSession();
        session.setAttribute("expenseId", expenseId);
        System.out.println("** expenseId " + expenseId);

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
//        out = response.getWriter();
//        out.println(expenseId);
//        out.flush();

        return "";
    }

    public static String getAllowencesDataSeparated(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        PrintWriter out = null;
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("paymentTypeId", "PAY_TYPE_SEPARATED");
        String str = "<select>";
        String allowenceType = "";
        try {

            List<GenericValue> list = delegator.findList("Allowences", EntityCondition.makeCondition(criteria), null, null, null, true);
            if (list.size() != 0) {
                for (GenericValue row : list) {
                    System.out.println("*1* allowenceType " + row.get("allowenceTypeId"));
                    allowenceType = getAllowenceTypeDescription(request, response, row.get("allowenceTypeId").toString());
                    System.out.println("*2* allowenceType " + allowenceType);
                    str += "<option value='" + row.get("allowenceId") + "'>" + row.get("description") + " - " + allowenceType + "</option>";
                }
            }
            str += "</select>";

            System.out.println("str " + str);

            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(str);
            out.flush();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";

    }
}
