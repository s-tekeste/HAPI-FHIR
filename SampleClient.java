import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;

import java.util.ArrayList;
import java.util.Collections;

public class SampleClient {

    public static void main(String[] theArgs) {

        // Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient client = fhirContext.newRestfulGenericClient( "http://hapi.fhir.org/baseR4" );
        client.registerInterceptor( new LoggingInterceptor( false ) );

        // Search for Patient resources
        Bundle responses = client
                .search()
                .forResource( "Patient" )
                .where( Patient.FAMILY.matches().value( "SMITH" ) )
                .returnBundle( Bundle.class )
                .execute();

        ArrayList<String> unsortedData = new ArrayList<String>();
        unsortedData =  collectPatientInfo( responses );
        printSortedData( unsortedData );

    }
        public static ArrayList<String> collectPatientInfo(Bundle responses){
            ArrayList<String> result = new ArrayList<String>();
            for (Bundle.BundleEntryComponent response : responses.getEntry()) { //for each loop saves every patient object in response
                Patient patient = (Patient) response.getResource(); //created a patient object

                HumanName patientName = (HumanName) patient.getName().get( 0 ); //access name array
                String patientGivenName = patientName.getGivenAsSingleString(); //retrieve first name
                String patientFamilyName = patientName.getFamily(); //retrieve last name
                String birthDate;

                if (patient.getBirthDate() != null) { //check if patient has a DOB on file
                    birthDate = patient.getBirthDate().toString(); }
                else {
                    birthDate = "No DOB"; }
                result.add(patientGivenName + " " + patientFamilyName + " " + birthDate ); //add each patients info to result
            }
            return result;
        }


        public static void printSortedData(ArrayList<String> p){
            Collections.sort( p ); //sorting patients by first name
            for (String i : p) {
                System.out.println( i );
            }
        }
    }









