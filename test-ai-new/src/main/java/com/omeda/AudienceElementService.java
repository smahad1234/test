package com.omeda;

public class AudienceElementService {

    @GET
    @Path("{" + OdysseyConstants.ELEMENT_ID + "}/test")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(PermissionConstants.ODY_APPLICATION_ACCESS_NEW_UI)
    @Operation(summary = "Load Audience Element.", description = "Load Audience Element data.")
    public AudienceElementSchema getAudience(@BeanParam RequestedDatabase requestDetail,
                                             @NotNull @PathParam(OdysseyConstants.ELEMENT_ID) Integer elementId) throws ParseException {

        System.out.println("testJaxRs getAudience called with elementId: " + elementId);
        return new AudienceElementSchema();

    }

}