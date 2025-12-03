package com.omeda;

public class AudienceElementService {

    @GET
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/test")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Load Audience Element.", description = "Load Audience Element data.")
    public AudienceElementSchema getAudience(@BeanParam RequestedDatabase requestDetail,
                                             @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId) throws ParseException {

        return new AudienceElementSchema();

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


}