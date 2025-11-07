package com.omeda.odyssey.ods.service;

import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

import com.omeda.bean.api.constants.BeanAPIParams;
import com.omeda.constants.OdysseyConstants;
import com.omeda.constants.PermissionConstants;
import com.omeda.ods.core.manager.OdsBeePluginManager;
import com.omeda.ods.audiencebuilder.schema.QuerySearchListSchema;
import com.omeda.ods.core.schema.IntCodeNameSchema;
import com.omeda.ods.emailbuilder.request.RequestedRegularDeplTracking;
import com.omeda.ods.emailbuilder.schema.DeploymentTypeSchema;
import com.omeda.ods.emailbuilder.schema.EmailDefaultsSchema;
import com.omeda.ods.emailbuilder.schema.RegularDeplTrackingSchema;
import com.omeda.ods.odyssey.request.*;
import com.omeda.ods.emailbuilder.request.*;
import com.omeda.ods.emailbuilder.schema.*;

import com.omeda.bean.api.BasicResponse;
import com.omeda.ods.odyssey.schema.*;

import com.omeda.ods.portal.request.RequestedPaginationSchema;
import com.omeda.ods.portal.schema.ListSchema;
import com.omeda.ods.sms.request.RequestedSmsSendToOne;
import com.omeda.ods.sms.request.RequestedSmsTester;
import com.omeda.ods.sms.request.RequestedSmsTesterPleat;
import com.omeda.ods.sms.request.RequestedSmsTrackingPleat;
import com.omeda.ods.sms.schema.*;
import io.swagger.v3.oas.annotations.Parameter;
import com.omeda.odyssey.ods.manager.*;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import com.omeda.constants.PortalAPIParams;
import com.omeda.ods.core.helper.ApplicationServiceBaseConstants.ApplicationEnum;
import com.omeda.ods.core.helper.OdsApplication;
import com.omeda.ods.core.request.RequestedDatabase;
import com.omeda.ods.core.request.RequestedDatabaseProfile;
import com.omeda.ods.core.service.OdsServiceBase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPartMediaTypes;

@Path("element")
@Produces("application/json")
@Tag(name="Odyssey", description="Request data to interact with Odyssey Elements.")
@OdsApplication(ApplicationEnum.ODYSSEY)
public class OdsElementService extends OdsServiceBase {
    public OdsElementService() {
        super();
    }

    @GET
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/audience")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Load Audience Element.", description = "Load Audience Element data.")
    public AudienceElementSchema getAudience(@BeanParam RequestedDatabase requestDetail,
                                     @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId) throws ParseException {
        OdsAudienceElementManager manager = new OdsAudienceElementManager();
        return manager.getAudienceElement(requestDetail, elementId);
    }

    @PATCH
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/audience")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Save Audience Element changes.",
            description="Update specific Audience Element data, not the whole element necessarily.")
    public BasicResponse saveAudienceElement(@BeanParam RequestedDatabase requestDetail,
                                             RequestedAudienceElementSave saveDetail,
                                             @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId) {
        OdsAudienceElementManager manager = new OdsAudienceElementManager();
        return manager.saveAudienceElement(requestDetail, saveDetail, elementId);
    }

    @POST
    @Deprecated (forRemoval = true, since = "/upload-file/external/source/{sourceType}/source-number/{sourceNumber}")
    @Path("audience")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(summary = "Upload an External File to an Audience Element.",
            description="Upload an External File to an Audience Element for an Odyssey Voyage.")
    public AudienceExternalListSchema audienceUploadFromExternalList(@Valid @BeanParam RequestedDatabaseProfile requestDetail,
            @Context HttpServletRequest request) {
    
        OdsDashboardManager odm = new OdsDashboardManager();
        return odm.audienceUploadExternalList(request, requestDetail.getEnvironmentId());
    }

    @GET
    @Path("output-criterias")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get Output Criterias.", description="Get a list of Output Criterias.")
    public QuerySearchListSchema getOutputCriteriaList(@BeanParam RequestedDatabaseProfile requestDetail) {
        OdsElementManager manager = new OdsElementManager();
        return manager.getOutputCriteriaList(requestDetail);
    }

    @GET
    @Path("selection-criteria/{" + OdysseyConstants.PARAM_SELECTION_CRITERIA_ID + "}/count")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Refresh Selection Criteria count.", description="Refresh the Selection Criteria counts for an element.")
    public Integer refreshAudienceCount(@BeanParam RequestedDatabase requestDetail,
                                        @NotNull @PathParam(OdysseyConstants.PARAM_SELECTION_CRITERIA_ID) Integer selectionCriteriaId) {
        OdsElementManager manager = new OdsElementManager();
        return manager.getSelectionCriteriaCounts(requestDetail, selectionCriteriaId);
    }

    @GET
    @Path("test-history")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Test history related to a Voyage Element.", 
        description="Provides test history related to a Voyage Element.")
    public ElementTestHistoryReportSchema getTestHistory(@Valid @BeanParam RequestedDatabase requestDetail,
            @NotEmpty @QueryParam(PortalAPIParams.TRACKING_NUMBER) String trackingNumber) {
    
        OdsElementManager manager = new OdsElementManager();
        return manager.getTestHistory(requestDetail, trackingNumber);
    }

    //Checked the front end � this API isn�t being used anywhere.
    /*@Deprecated
    @GET
    @Path("email")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Email Element of Odyssey Voyage.")
    @ApiResponse(description="Provides an initial data after email element loads.")
    public List<EmailElementSchema> loadsEmailData(@Valid @BeanParam RequestedDatabaseProfile requestDetail,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response) {

        OdsDashboardManager odm = new OdsDashboardManager();
        return odm.emailElement(requestDetail.getEnvironmentId(), requestDetail.getDataViewId());
    }*/

    @GET
    @Path("email/deployment-types")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "List of DeploymentTypes",
            description="Provides deployment types for a given environmentId.")
    @ApiResponse(description="List of DeploymentTypes.")
    public List<DeploymentTypeSchema> getDeploymentTypes (@Valid @BeanParam RequestedDatabaseProfile requestDetail)
    {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.getDeploymentTypes(requestDetail.getEnvironmentId(), requestDetail.getDataViewId());
    }

    @GET
    @Path("email/email-default")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Get the Email Default details for a particular Deployment type.", description="Get the Email Default details for a particular Deployment type.")
    public EmailDefaultsSchema getEmailDefaultDetails (@BeanParam RequestedDatabase requestDetail,
                                                       @NotNull @QueryParam(OdysseyConstants.PARAM_DEPLOYMENT_TYPE_ID) Integer deploymentTypeId
    ) {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.getEmailDefaultDetails(requestDetail.getEnvironmentId(), deploymentTypeId);
    }

    @GET
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/email")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Get the Email Element details of Odyssey Voyage.", description="Get the Email Element details of Odyssey Voyage.")
    public EmailElementSchema getEmailElementDetails (@BeanParam RequestedDatabase requestDetail,
                                                       @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId)
    {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.getEmailElementDetails(requestDetail.getEnvironmentId(), elementId);
    }

    @GET
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/email/link-tracking")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Get the Link tracking details of Odyssey Email element.", description="Get the Link tracking details of Odyssey Email element.")
    public RegularDeplTrackingSchema getLinkTrackingDetails (@BeanParam RequestedDatabase requestDetail,
                                                             @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId)
    {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.getLinkTrackingDetails(requestDetail.getEnvironmentId(), elementId);
    }

    @GET
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/email/tester-seed")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Get the Testers & Seeds details of Odyssey Email element.", description="Get the Testers & Seeds details of Odyssey Email element.")
    public EmailElementTestSeedSchema getTesterSeedDetails(@BeanParam RequestedDatabase requestDetail,
                                                                            @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId)
    {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.getTesterSeedDetails(requestDetail.getEnvironmentId(), elementId);
    }

    @PUT
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/email")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Save the Email Element details of Odyssey Voyage.", description="Save the Email Element details of Odyssey Voyage.")
    public EmailElementSchema saveEmailElementDetails (@BeanParam RequestedDatabase requestDetail,
                                                       @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId,
                                                       @Valid RequestedEmailElement requestedEmailElement)
    {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.saveEmailElementDetails(requestDetail.getEnvironmentId(), elementId, requestedEmailElement);
    }

    @POST
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/email/message-content/url")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Upload message Content via URL for Odyssey Email element.", description="Upload message Content via URL for Odyssey Email element.")
    public UploadedMessageContentSchema retrieveContentViaUrl (@BeanParam RequestedDatabase requestDetail,
                                                               @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId,
                                                               @Valid RequestedRetrieveMsgContent requestedRetrieveMsgContent)
    {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.retrieveContentViaUrl(requestDetail.getEnvironmentId(), elementId, requestedRetrieveMsgContent);
    }

    @PUT
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/email/link-tracking")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Save the Link tracking details of Odyssey Email element.", description="Save the Link tracking details of Odyssey Email element.")
    public RegularDeplTrackingSchema saveLinkTrackingDetails (@BeanParam RequestedDatabase requestDetail,
                                                              @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId,
                                                              @Valid RequestedRegularDeplTracking requestedDeplTracking)
    {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.saveLinkTrackingDetails(requestDetail.getEnvironmentId(), elementId, requestedDeplTracking);
    }

    @PUT
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/email/tester-seed")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Save the Testers & Seeds details of Odyssey Email element.", description="Save the Testers & Seeds details of Odyssey Email element.")
    public EmailElementTestSeedSchema saveTesterSeedDetails (@BeanParam RequestedDatabase requestDetail,
                                                       @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId,
                                                       @Valid RequestedEmailElementTestSeed requestedEmailElementTestSeed)
    {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.saveTesterSeedDetails(requestDetail.getEnvironmentId(), elementId, requestedEmailElementTestSeed);
    }


    @POST
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/email/tester-seed/contact-search")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Contact Search Test, and Seed Recipients of Odyssey Email element.", description="Contact Search Test, and Seed Recipients of Odyssey Email element.")
    public RegularDeplTestSeedRecipientsContactSearchSchema contactSearchTestSeed (@BeanParam RequestedDatabase requestDetail,
                                                                                     @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId,
                                                                                     @Valid RequestedRegularTestSeedContactSearch requestedRegularTestSeedContactSearch)
    {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.contactSearchTestSeed(requestDetail.getEnvironmentId(), elementId, requestedRegularTestSeedContactSearch);
    }

    @POST
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/email/tester-seed/contact-search/add-seed")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Add Seed from Contact Search list of the Test, and Seed of Odyssey Email element.", description="Add Seed from Contact Search list of the Test, and Seed of Odyssey Email element.")
    public BasicResponse addContactSearchTestSeed (@BeanParam RequestedDatabase requestDetail,
                                                   @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId,
                                                   @Valid @BeanParam RequestedTestSeedId testSeedId)
    {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.addContactSearchTestSeed(requestDetail.getEnvironmentId(), elementId, testSeedId);
    }

    @POST
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/email/tester-seed/add-seed-from-form")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Add New Seed to the Test, and Seed Recipients of Odyssey Email element.", description="Add New Seed to the Test, and Seed Recipients of Odyssey Email element.")
    public BasicResponse addNewTestSeedFromForm (@BeanParam RequestedDatabase requestDetail,
                                         @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId,
                                         @Valid RequestedRegularTestSeedSaveNewSeed requestedRegularTestSeedSaveNewSeed)
    {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.addNewTestSeedFromForm(requestDetail.getEnvironmentId(), elementId, requestedRegularTestSeedSaveNewSeed);
    }

    @POST
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/email/send-to-one")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Send to me or Send to One of Odyssey Email element.", description="Send to me or Send to One of Odyssey Email element.")
    public RegDeplSendTestToOneSchema sendToOne (@BeanParam RequestedDatabase requestDetail,
                                                 @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId,
                                                 @Valid RequestedSendToOne requestedSendToOne)
    {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.sendToOne(requestDetail.getEnvironmentId(), elementId, requestedSendToOne);
    }

    @POST
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/email/send-test")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Send Test of Odyssey Email element.", description="Send Test of Odyssey Email element.")
    public RegDeplSendTestSchema sendTest (@BeanParam RequestedDatabase requestDetail,
                                           @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId,
                                           @Valid RequestedRegDeplSentTest requestedSendTest)
    {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.sendTest(requestDetail.getEnvironmentId(), elementId, requestedSendTest);
    }

    @GET
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/email/send-test/continue")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Continue functionality of Send Test of Odyssey Email element.", description="Continue functionality of Send Test of Odyssey Email element.")
    public RegDeplSendTestSchema continueSendTest (@BeanParam RequestedDatabase requestDetail,
                                                   @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId,
                                                   @QueryParam("isUnsubscribe") String isUnsubscribe,
                                                   @QueryParam("isNonMergeValueForLinkPara") String isNonMergeValueForLinkPara )
    {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.continueSendTest(requestDetail.getEnvironmentId(), elementId, isUnsubscribe, isNonMergeValueForLinkPara);
    }

    @GET
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/email/link-tracking/default-category")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Get the default categories for Link tracking of Odyssey Email element.", description="Get the default categories for Link tracking of Odyssey Email element.")
    public List<RegularDefaultCategorySchema> getLinkTrackingDefaultCategories (@BeanParam RequestedDatabase requestDetail,
                                                             @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId)
    {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.getLinkTrackingDefaultCategories(requestDetail.getEnvironmentId(), elementId);
    }

    @GET
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/email/link-tracking/retrieve-link-names")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Get the link names for Link tracking of Odyssey Email element.", description="Get the link names for Link tracking of Odyssey Email element.")
    public List<LinkNamesSchema> retrieveLinkNames (@BeanParam RequestedDatabase requestDetail,
                                                                                @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId)
    {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.retrieveLinkNames(requestDetail.getEnvironmentId(), elementId);
    }

    @PUT
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/status-code/{" + BeanAPIParams.STATUS_CODE + "}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Update Element status.",
            description="Update a specific Element's Status Code.")
    public BasicResponse changeStatusCode(@BeanParam RequestedDatabase requestDetail,
                         @NotNull @PathParam(BeanAPIParams.STATUS_CODE) Integer statusCode,
                         @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId) {
        OdsElementManager manager = new OdsElementManager();
        return manager.changeStatusCode(requestDetail, elementId, statusCode);
    }

    @GET
    @Path("bee/auth-token")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get Bee Token for plugin", description="Get Bee Token for plugin")
    public BeeTokenSchema getBeeAuthToken(@BeanParam RequestedDatabase requestDetail) {
        OdsBeePluginManager pluginManager = new OdsBeePluginManager();
        return pluginManager.getBeeTokenInfo(requestDetail, ApplicationEnum.ODYSSEY.getDbName().toLowerCase());
    }

    @POST
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/upload-file/external/source/{sourceType}/source-number/{sourceNumber}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({MediaType.APPLICATION_JSON, MultiPartMediaTypes.MULTIPART_MIXED})
    @Operation(summary = "Processes file data submitted from audience and suppression sources.",
            description="Processes file data submitted from audience and suppression sources.")
    public SourceUploadSchema saveExternalFile(
            @FormDataParam("dataFile") InputStream dataFileInStream,
            @FormDataParam("dataFile") FormDataContentDisposition dataFileDetails,

            @BeanParam RequestedDatabase requestDetail,

            @NotNull
            @PathParam(OdysseyConstants.ELEMENT_ID) int elementId,

            @NotEmpty
            @Parameter(description = "What source this file belongs to. Valid values are audience or suppression.")
            @PathParam("sourceType") String sourceType,

            @Parameter(description = "The number of the audience source.")
            @PathParam("sourceNumber") int sourceNumber,

            @Context HttpServletRequest request) throws WebApplicationException {
        if (sourceType==null) {
            throw new BadRequestException("sourceType must be specified.");
        } else if (!sourceType.equalsIgnoreCase(OdsAudienceElementManager.SOURCE_AUDIENCE) &&
                !sourceType.equalsIgnoreCase(OdsAudienceElementManager.SOURCE_SUPPRESSION)) {
            throw new BadRequestException("Invalid sourceType submitted.");
        }

        if (sourceNumber<1) {
            throw new BadRequestException("Invalid sourceNumber submitted.");
        }

        OdsAudienceElementManager manager = new OdsAudienceElementManager();
        return manager.saveExternalFile(request, requestDetail, dataFileInStream, dataFileDetails,
                elementId, sourceType, sourceNumber);
    }

    @GET
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/upload-files/internal")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get internal audience source files.",
            description="Get a list of internal audience source files.")
    public List<String> getInternalSourceFileList(@BeanParam RequestedDatabase requestDetail,
                @NotNull
                @PathParam(OdysseyConstants.ELEMENT_ID) int elementId) throws WebApplicationException {
        OdsAudienceElementManager manager = new OdsAudienceElementManager();
        return manager.getInternalSourceFileList(requestDetail, elementId);
    }

    @GET
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/upload-file/internal/source-number/{sourceNumber}")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get file data from an internal audience source.",
            description="Get file data details for a single internal audience source.")
    public SourceUploadSchema getInternalSourceFileDetails(@BeanParam RequestedDatabase requestDetail,
                                    @PathParam(OdysseyConstants.ELEMENT_ID) int elementId,

                                    @Parameter(description = "The number of the audience source.")
                                    @PathParam("sourceNumber") int sourceNumber,

                                    @NotEmpty
                                    @Parameter(description = "The name of the file to retrieve.")
                                    @QueryParam(BeanAPIParams.FILENAME) String fileName) throws WebApplicationException {
        OdsAudienceElementManager manager = new OdsAudienceElementManager();
        return manager.getInternalSourceFileDetails(requestDetail, elementId, fileName, sourceNumber);
    }

    @GET
    @Path("sms/message-types")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "List of SMS MessageTypes",
            description="Provides SMS message types for a given environmentId.")
    @ApiResponse(description="List of SMS MessageTypes.")
    public List<SmsMessageTypeSchema> getSmsMessageTypes (@Valid @BeanParam RequestedDatabaseProfile requestDetail)
    {
        OdsSmsElementManager mgr = new OdsSmsElementManager();
        return mgr.getSmsMessageTypes(requestDetail.getEnvironmentId());
    }

    @GET
    @Path("sms/merge-variables")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Get the standard SMS merge variables for SMS Element of Odyssey Voyage.", description="Get the standard SMS merge variables for SMS Element of Odyssey Voyage.")
    public List<SmsMergeVariableSchema> getSmsElementStandardMergeVars (@BeanParam RequestedDatabase requestDetail)
    {
        OdsSmsElementManager mgr = new OdsSmsElementManager();
        return mgr.getSmsElementStandardMergeVars(requestDetail.getEnvironmentId());
    }

    @GET
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/sms")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Get the SMS Element details of Odyssey Voyage.", description="Get the SMS Element details of Odyssey Voyage.")
    public SmsElementSchema getSmsElementDetails (@BeanParam RequestedDatabase requestDetail,
                                              @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId)
    {
        OdsSmsElementManager mgr = new OdsSmsElementManager();
        return mgr.getSmsElementDetails(requestDetail.getEnvironmentId(), elementId);
    }

    @GET
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/sms/link-tracking")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Get the link tracking details of Odyssey SMS element.", description="Get the Link tracking details of Odyssey SMS element.")
    public SmsTrackingPleatSchema getSmsLinkTrackingDetails (@BeanParam RequestedDatabase requestDetail,
                                                             @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId)
    {
        OdsSmsElementManager mgr = new OdsSmsElementManager();
        return mgr.getSmsLinkTrackingDetails(requestDetail.getEnvironmentId(), elementId);
    }

    @GET
    @Path("sms/web-tracking/parameters")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Get available Web Tracking Parameters for link tracking of Odyssey SMS element.", description="Get available Web Tracking Parameters for link tracking of Odyssey SMS element.")
    public SmsWebtrackingParameterListSchema getWebTrackingParameters()
    {
        OdsSmsElementManager mgr = new OdsSmsElementManager();
        return mgr.getWebTrackingParameters();
    }

    @GET
    @Path("sms/web-tracking")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Get all Web Tracking Configurations for link tracking of Odyssey SMS element.", description="Get all Web Tracking Configurations for link tracking of Odyssey SMS element.")
    public List<IntCodeNameSchema> getAllWebTrackingConfigurations(@BeanParam RequestedDatabase requestDetail)
    {
        OdsSmsElementManager mgr = new OdsSmsElementManager();
        return mgr.getAllWebTrackingConfigurations(requestDetail.getEnvironmentId());
    }

    @GET
    @Path("sms/web-tracking/config/{webConfigId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Get selected Web Tracking Configuration for link tracking of Odyssey SMS element.", description="Get selected Web Tracking Configuration for link tracking of Odyssey SMS element.")
    public SmsWebtrackingConfigSchema getWebTrackingConfiguration(@BeanParam RequestedDatabase requestDetail,
                                                                  @PathParam("webConfigId") int webConfigId)
    {
        OdsSmsElementManager mgr = new OdsSmsElementManager();
        return mgr.getWebTrackingConfiguration(requestDetail.getEnvironmentId(), webConfigId);
    }

    @GET
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/sms/tester-seed")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Get the Testers & Seeds details of Odyssey SMS element.", description="Get the Testers & Seeds details of Odyssey SMS element.")
    public SmsTesterPleatSchema getSmsTesterSeedDetails(@BeanParam RequestedDatabase requestDetail,
                                                        @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId)
    {
        OdsSmsElementManager mgr = new OdsSmsElementManager();
        return mgr.getSmsTesterSeedDetails(requestDetail.getEnvironmentId(), elementId);
    }

    @GET
    @Path("sms/testers")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Get a list of testers for Testers & Seeds of Odyssey SMS element.", description="Returns a list of testers for Testers & Seeds of Odyssey SMS element.")
    public ListSchema<SmsTesterSchema> getTesters(@BeanParam RequestedDatabase requestDetail,
                                                  @BeanParam RequestedPaginationSchema paginationSchema,
                                                  @QueryParam("firstName") String firstName,
                                                  @QueryParam("lastName") String lastName,
                                                  @QueryParam("phoneNumber") String phoneNumber)
    {
        OdsSmsElementManager mgr = new OdsSmsElementManager();
        return mgr.getTesters(requestDetail.getEnvironmentId(), paginationSchema, firstName, lastName, phoneNumber);
    }

    @PUT
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/sms")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Save the SMS Element details of Odyssey Voyage.", description="Save the SMS Element details of Odyssey Voyage.")
    public SmsElementSchema saveSmsElementDetails (@BeanParam RequestedDatabase requestDetail,
                                                       @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId,
                                                       @Valid RequestedSmsElement requestedSmsElement)
    {
        OdsSmsElementManager mgr = new OdsSmsElementManager();
        return mgr.saveSmsElementDetails(requestDetail.getEnvironmentId(), elementId, requestedSmsElement);
    }

    @PUT
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/sms/link-tracking")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Save the Link tracking details of Odyssey SMS element.", description="Save the Link tracking details of Odyssey SMS element.")
    public SmsTrackingPleatSchema saveSmsLinkTrackingDetails (@BeanParam RequestedDatabase requestDetail,
                                                              @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId,
                                                              @Valid RequestedSmsTrackingPleat requestedSmsTracking)
    {
        OdsSmsElementManager mgr = new OdsSmsElementManager();
        return mgr.saveSmsLinkTrackingDetails(requestDetail.getEnvironmentId(), elementId, requestedSmsTracking);
    }

    @PUT
    @Path("sms/web-tracking/config")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Save Web Tracking Configuration for link tracking of Odyssey SMS element.", description="Save Web Tracking Configuration for link tracking of Odyssey SMS element.")
    public SmsWebtrackingConfigSchema saveWebTrackingConfiguration (@BeanParam RequestedDatabase requestDetail,
                                                              @Valid SmsWebtrackingConfigSchema webTrackingConfig)
    {
        OdsSmsElementManager mgr = new OdsSmsElementManager();
        return mgr.saveWebTrackingConfiguration(requestDetail.getEnvironmentId(), webTrackingConfig);
    }

    @POST
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/sms/tester-seed/tester")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Create a tester and assign to Odyssey SMS element.", description="Create a tester and assign to Odyssey SMS element.")
    public SmsTesterSchema saveSmsTester (@BeanParam RequestedDatabase requestDetail,
                                          @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId,
                                          @Valid RequestedSmsTester testRequest)
    {
        OdsSmsElementManager mgr = new OdsSmsElementManager();
        return mgr.saveSmsTester(requestDetail.getEnvironmentId(), elementId, testRequest);
    }

    @PUT
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/sms/tester-seed/tester/{" + OdysseyConstants.TESTER_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Assign an existing tester to Odyssey SMS element.", description="Assign an existing tester to Odyssey SMS element.")
    public SmsTesterSchema assignExistingTester (@BeanParam RequestedDatabase requestDetail,
                                         @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId,
                                         @NotNull @PathParam(OdysseyConstants.TESTER_ID) int testerId)
    {
        OdsSmsElementManager mgr = new OdsSmsElementManager();
        return mgr.assignExistingTester(requestDetail.getEnvironmentId(), elementId, testerId);
    }

    @PUT
    @Path("sms/tester-seed/tester/{" + OdysseyConstants.TESTER_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Update an existing tester for Odyssey SMS element.", description="Update an existing tester for Odyssey SMS element.")
    public SmsTesterSchema updateSmsTester (@BeanParam RequestedDatabase requestDetail,
                                            @Valid RequestedSmsTester testRequest,
                                            @NotNull @PathParam(OdysseyConstants.TESTER_ID) int testerId)
    {
        OdsSmsElementManager mgr = new OdsSmsElementManager();
        return mgr.updateSmsTester(requestDetail.getEnvironmentId(), testerId, testRequest);
    }

    @PUT
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/sms/tester-seed")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Save the Testers & Seeds details of Odyssey SMS element.", description="Save the Testers & Seeds details of Odyssey SMS element.")
    public SmsTesterPleatSchema saveSmsTesterSeedDetails(@BeanParam RequestedDatabase requestDetail,
                                                         @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId,
                                                         @Valid RequestedSmsTesterPleat testRequest)
    {
        OdsSmsElementManager mgr = new OdsSmsElementManager();
        return mgr.saveSmsTesterSeedDetails(requestDetail.getEnvironmentId(), elementId, testRequest);
    }

    @PUT
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/sms/send-to-one")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Send to One of Odyssey SMS element.", description="Send to One of Odyssey SMS element.")
    public BasicResponse sendToOne (@BeanParam RequestedDatabase requestDetail,
                                    @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId,
                                    @Valid RequestedSmsSendToOne requestedSmsSendToOne)
    {
        OdsSmsElementManager mgr = new OdsSmsElementManager();
        return mgr.sendToOne(requestDetail.getEnvironmentId(), elementId, requestedSmsSendToOne);
    }

    @PUT
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/sms/send-test")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Send Test of Odyssey SMS element.", description="Send Test of Odyssey SMS element.")
    public BasicResponse sendTest (@BeanParam RequestedDatabase requestDetail,
                                           @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId)
    {
        OdsSmsElementManager mgr = new OdsSmsElementManager();
        return mgr.sendTest(requestDetail.getEnvironmentId(), elementId);
    }
    @GET
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/fatigue-filter")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Load a Fatigue Filter Element.", description = "Load Fatigue Filter Element data.")
    public FatigueFilterElementSchema getFatigueFilter(@BeanParam RequestedDatabase requestDetail,
                                             @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId) {
        OdsFatigueFilterElementManager manager = new OdsFatigueFilterElementManager();
        return manager.getFatigueFilter(requestDetail, elementId);
    }

    @PATCH
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/fatigue-filter")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Save a Fatigue Filter Element.", description = "Save a parameter for a Fatigue Filter Element.")
    public BasicResponse saveFatigueFilter(@BeanParam RequestedDatabase requestDetail,
                                                        RequestedFatigueFilterElementSave saveDetail,
                                                        @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId) {
        OdsFatigueFilterElementManager manager = new OdsFatigueFilterElementManager();
        return manager.saveFatigueFilter(requestDetail, saveDetail, elementId);
    }

    @GET
    @Path("bee/rows")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Get the Bee saved rows.", description="Get the Bee saved rows.")
    public String getBeeRows(@BeanParam RequestedDatabase requestDetail) {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.getBeeRows(requestDetail.getEnvironmentId());
    }

    @GET
    @Path("bee/row/{guid}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Get a Bee saved row.", description="Get a Bee saved row.")
    public String getBeeRow(@BeanParam RequestedDatabase requestDetail,
                            @NotNull @PathParam(BeanAPIParams.GUID) String guid) {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.getBeeRow(requestDetail.getEnvironmentId(), guid);
    }

    @POST
    @Path("bee/row")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Save a Bee row.",
            description="Save a Bee row.")
    public BasicResponse saveBeeRow(@Valid @BeanParam RequestedDatabase requestDetail,
                                    @Valid RequestedBeeRow requestBeeRow) {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.saveBeeRow(requestDetail.getEnvironmentId(), requestBeeRow);
    }

    @DELETE
    @Path("bee/row/{guid}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Delete a Bee row.",
            description="Delete a Bee row.")
    public BasicResponse saveBeeRow(@Valid @BeanParam RequestedDatabase requestDetail,
                                    @NotNull @PathParam(BeanAPIParams.GUID) String guid) {
        OdsEmailElementManager mgr = new OdsEmailElementManager();
        return mgr.deleteBeeRow(requestDetail.getEnvironmentId(), guid);
    }
}

