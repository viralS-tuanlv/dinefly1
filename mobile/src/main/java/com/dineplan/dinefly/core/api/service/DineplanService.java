package com.dineplan.dinefly.core.api.service;

import com.dineplan.dinefly.core.api.model.api.bill.BillData;
import com.dineplan.dinefly.core.api.model.api.in.DinaplanMoveTableResult;
import com.dineplan.dinefly.core.api.model.api.in.DinaplanTicketResult;
import com.dineplan.dinefly.core.api.model.api.in.DineplanAuthResult;
import com.dineplan.dinefly.core.api.model.api.in.DineplanComboMenuResult;
import com.dineplan.dinefly.core.api.model.api.in.DineplanGuestQuestionnarieResponse;
import com.dineplan.dinefly.core.api.model.api.in.DineplanMenuResult;
import com.dineplan.dinefly.core.api.model.api.in.DineplanPrintDataResult;
import com.dineplan.dinefly.core.api.model.api.in.DineplanSoldoutCheckResult;
import com.dineplan.dinefly.core.api.model.api.in.DineplanTableSummaryResult;
import com.dineplan.dinefly.core.api.model.api.in.DineplanTablesResult;
import com.dineplan.dinefly.core.api.model.api.in.GuestFeedback;
import com.dineplan.dinefly.core.api.model.api.out.DineplanMoveTableRequest;
import com.dineplan.dinefly.core.api.model.api.out.DineplanSoldoutCheckRequest;
import com.dineplan.dinefly.core.api.model.api.out.DineplanSoldoutCheckResponse;
import com.dineplan.dinefly.core.api.model.api.out.DineplanTicketUpdateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DineplanService
{
    @POST("auth")
    @FormUrlEncoded
    Call<DineplanAuthResult> auth(@Field("Password") String password);

    @GET("menu/flym")
    Call<DineplanMenuResult> getMenu();

    @GET("menu/flycombo")
    Call<DineplanComboMenuResult> getCombos();

    @GET("entity/all")
    Call<DineplanTablesResult> getTables();

    @GET("entity/single/{id}")
    Call<DineplanTableSummaryResult> getTableOverview(@Path("id") int tableId);

    @GET("flyticket/single/{id}")
    Call<DinaplanTicketResult> getTicket(@Path("id") long ticketId);

    @POST("flyticket/push")
    @Headers({"Content-Type: application/json"})
    Call<DinaplanTicketResult> updateTicket(@Body DineplanTicketUpdateRequest data);

    @POST("flyticket/checksoldouts")
    @Headers({"Content-Type: application/json"})
    Call<DineplanSoldoutCheckResult> checkForSoldouts(@Body DineplanSoldoutCheckRequest data);

    @POST("flyticket/uentity")
    @Headers({"Content-Type: application/json"})
    Call<DinaplanMoveTableResult> moveTable(@Body DineplanMoveTableRequest data);

    @GET("print/bill/{id}")
    Call<Void> printBill(@Path("id") long ticketId);

    @GET("flyticket/preparation/{id}")
    Call<DineplanPrintDataResult> getPreparationOrderPrintoutData(@Path("id") long ticketId);

    @GET("flyticket/billcontent/{id}")
    Call<DineplanPrintDataResult> getTicketBillPrintoutData(@Path("id") long ticketId);

    @POST("feedback/push")
    @Headers({"Content-Type: application/json"})
    Call<Void> submitGuestFeedback(@Body GuestFeedback feedback);

    @GET("feedback/retrieve")
    @Headers({"Content-Type: application/json"})
    Call<DineplanGuestQuestionnarieResponse> getGuestFeedbackQuestionnarie();

    @GET("flyticket/billcontentoutput/{id}")
    Call<BillData> getBillContent(@Path("id") long ticketId);

}
