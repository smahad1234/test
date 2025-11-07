package com.omeda;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

public class testJaxRs {
    @GET
    @Path("{/upload-file/internal/source-number/{sourceNumber}")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Get file data from an internal audience source.",
            description="Get file data details for a single internal audience source.")
    public void getInternalSourceFileDetails() {
        System.out.println("test jax rs");
    }

}