# Polish Visa Center
This is a RESTful web-service simulating the work processes of the Polish Visa Center in Belarus.

<h2>Technologies:</h2>
<ul>
  <li>Java 8</li>
  <li>Spring (Boot, Web, Data, Security)</li>
  <li>PostgreSQL</li>
  <li>Flyway</li>
  <li>Maven</li>
  <li>JWT</li>
  <li>JSON</li>
  <li>HATEOAS</li>
  <li>log4j2</li>
  <li>Spring Boot Test</li>
  <li>JUnit</li>
  <li>Lombok</li>
</ul>

<h2>Domain structure & Functionality:</h2>
<img src="images/func.png">

<h2>Client's actions:</h2>
<img src="images/client_logic.png">

<h2>DB model:</h2>
<img src="images/db_struct.png">

<h2>Resources:</h2>

<h4>Registration / Login</h4>
<table>
  <thead>
    <tr>
      <th>Method</th>
      <th>Endpoint</th>
      <th>Access</th>
      <th>Description</th>
      <th>Request</th>
      <th>Response</th>
    </tr>
  </thead>
  <tr>
    <td>POST</td>
    <td>/registration</td>
    <td>ALL</td>
    <td>Registration of the user with the role CLIENT.</td>
    <td>
        <pre>
          header: Content-Type: application/json
          body: {
                  "firstName": "JOHN",
                  "lastName": "DOE",
                  "email": "john_doe@gmail.com",
                  "phoneNumber": "375291001010", 
                  "password": "password123",
                  "dateOfBirth": "1980-10-10",
                  "passportId": "0101PSPID10001010",
                  "occupation": "EMPLOYED",
                  "personalDataProcAgreement": true
                }
         </pre>
    </td>
    <td>
      If ok:
        <pre>
          header: Status Code: 201 Created
                  Location: http://localhost:8080/my-visa-application
                  Authorization: Bearer &lt;token&gt;
        </pre>
      If form filling is incorrect - validation failed:
        <pre>
          header: Status Code: 406 Not Acceptable
          body: {
                  "fieldErrors": [
                    {
                      "field": "phoneNumber",
                               "message": "Phone number should contain code and number itself, e.g. 375291001010."
                    },
                    {
                      "field": "firstName",
                               "message": "Only uppercase latin letters, spaces and dashes are allowed."
                    }
                  ]
                }
        </pre>
      If any field is absent:
        <pre>
          header: Status Code: 400 Bad Request
          body: The form filled incorrectly.
        </pre>
      If passport ID and/or email is already in DB:
        <pre>
          header: Status Code: 400 Bad Request
          body: There is another client in the database with such email and/or passport ID.
        </pre>
    </td>
  </tr>
  <tr>
    <td>POST</td>
    <td>/login</td>
    <td>ALL</td>
    <td>Login for all roles.</td>
    <td>
      <pre>
        header: Content-Type: application/json
        body: {
                "username": "john_doe@gmail.com",
                "password": "password123"
              }
      </pre>
    </td>
    <td>
      If ok:
        <pre>
          header: Status Code: 200 OK
                  Location: http://localhost:8080/&lt;depends_on_user's_role&gt;
                  Authorization: Bearer &lt;token&gt;
        </pre>
      If login data are incorrect:
        <pre>
          header: Status Code: 400 Bad Reqguest
          body: Bad credentials
        </pre>
    </td>
  </tr>
</table>

<h4>Managing employees by DIRECTOR and MANAGER</h4>
<table>
 <thead>
   <tr>
     <th>Method</th>
     <th>Endpoint</th>
     <th>Access</th>
     <th>Description</th>
     <th>Request</th>
     <th>Response</th>
   </tr>
 </thead>
 <tr>
   <td>POST</td>
   <td>/employees</td>
   <td>DIRECTOR, MANAGER</td>
   <td>Add new employee (all - for DIRECTOR, only operators from manager's city - for MANAGER).</td>
   <td>
       <pre>
         header: Content-Type: application/json
                 Authorization: Bearer &lt;token&gt;
         body: {
                 "firstName": "FOO",
                 "lastName": "BAR",
                 "email": "foo_bar@visacenter.com",
                 "phoneNumber": "375292002020",
                 "password": "password123",
                 "position": "MANAGER",
                 "city": "MINSK"
               }
     </pre>
   </td>
   <td>
     If ok:
     <pre>
       header: Status Code: 201 Created
       body: {
                 "firstName": "FOO",
                 "lastName": "BAR",
                 "email": "foo_bar@visacenter.com",
                 "phoneNumber": "375292002020",
                 "position": "MANAGER",
                 "city": "MINSK",
                 "_links": {
                     "self": {
                         "href": "http://localhost:8080/employees/2"
                     },
                     "employees": {
                         "href": "http://localhost:8080/employees{?city,position,lastName,page,size,sort}",
                         "templated": true
                     }
                 }
             }
     </pre>
     If form filling is incorrect - validation failed:
     <pre>
        header: Status Code: 406 Not Acceptable
        body: {
                "fieldErrors": [
                  {
                    "field": "phoneNumber",
                             "message": "Phone number should contain code and number itself, e.g. 375291001010."
                  },
                  {
                    "field": "firstName",
                             "message": "Only uppercase latin letters, spaces and dashes are allowed."
                  }
                ] 
              }
     </pre>
     If any field is absent:
       <pre>
         header: Status Code: 400 Bad Request
         body: The form filled incorrectly.
       </pre>
     If email is already in DB:
       <pre>
         header: Status Code: 400 Bad Request
         body: There is another employee in the database with such email.
       </pre>
     If MANAGER tried to add not operator and/or not from manager's city:
       <pre>
         header: Status Code: 400 Bad Request
         body: You can add only operators in &lt;manager's_city&gt;.
       </pre>
   </td>
 <tr>
   <td>GET</td>
   <td>/employees</td>
   <td>DIRECTOR, MANAGER</td>
   <td>Get all employees (all - for DIRECTOR, only operators from manager's city - for MANAGER).<br />
       Filtering is available by following parameters: city, position, lastName. Also by word fragment, e.g. <i>?lastName=do</i> -> all entries with last names containing "do".<br />
       Sorting and paging are available.
   </td>
   <td>
       <pre>
         header: Content-Type: application/json
                 Authorization: Bearer &lt;token&gt;
       </pre>
   </td>
   <td>
     If ok:
     <pre>
       header: Status Code: 200 OK
       body: {
               "_embedded": {
                 "employees": [
                           {
                             "firstName": "ADMIN",
                             "lastName": "ADMIN",
                             "email": "admin@visacenter.com",
                             "phoneNumber": "375291001010",
                             "position": "DIRECTOR",
                             "city": "MINSK",
                             "_links": {
                                 "self": {
                                     "href": "http://localhost:8080/employees/1"
                                 },
                                 "employees": {
                                     "href": "http://localhost:8080/employees{?city,position,lastName,page,size,sort}",
                                     "templated": true
                                 }
                             }
                         },
                         {                     
                             "firstName": "FOO",
                             "lastName": "BAR",
                             "email": "foo_bar@visacenter.com",
                             "phoneNumber": "375292002020",
                             "position": "MANAGER",
                             "city": "MINSK",
                             "_links": {
                                 "self": {
                                     "href": "http://localhost:8080/employees/2"
                                 },
                                 "employees": {
                                     "href": "http://localhost:8080/employees{?city,position,lastName,page,size,sort}",
                                     "templated": true
                                 }
                             }
                         }
                     ]
                 },
                 "_links": {
                     "self": {
                         "href": "http://localhost:8080/employees?page=0&size=10&sort=lastName,asc"
                     }
                 },
                 "page": {
                     "size": 10,
                     "totalElements": 2,
                     "totalPages": 1,
                     "number": 0
                 }
             }
     </pre>
     If not found:
     <pre>
       header: Status Code: 404 Not Found
       body: Cannot find employees. / Cannot find employees by search criteria. / Cannot find operators in &lt;manager's_
             city&gt; with last name '&lt;operator's_last_name&gt;'.
     </pre>
     If MANAGER tried to search not operators from manager's city:
     <pre>
       header: Status Code: 400 Bad Request
       body: You can search only operators in &lt;manager's_city&gt; by last name.
     </pre>
   </td>
 </tr>
 <tr>
   <td>GET</td>
   <td>/employees/{id}</td>
   <td>DIRECTOR, MANAGER</td>
   <td>Get employee by ID (all - for DIRECTOR, only operators from manager's city - for MANAGER).</td>
   <td>
     <pre>
       header: Content-Type: application/json
               Authorization: Bearer &lt;token&gt;
     </pre>
   </td>
   <td>
     If ok:
     <pre>
       header: Status Code: 200 OK
       body: {
                 "firstName": "ADMIN",
                 "lastName": "ADMIN",
                 "email": "admin@visacenter.com",
                 "phoneNumber": "375291001010",
                 "position": "DIRECTOR",
                 "city": "MINSK",
                 "_links": {
                     "self": {
                         "href": "http://localhost:8080/employees/1"
                     },
                     "employees": {
                         "href": "http://localhost:8080/employees{?city,position,lastName,page,size,sort}",
                         "templated": true
                     }
                 }
             }
     </pre>
     If not found:
     <pre>
       header: Status Code: 404 Not Found
       body: Cannot find employee with ID = &lt;ID&gt;. / Cannot find operator in &lt;manager's_city&gt; with ID = &lt;ID&gt;.
     </pre>
   </td>
 </tr>
 <tr>
   <td>PUT</td>
   <td>/employees/{id}</td>
   <td>DIRECTOR, MANAGER</td>
   <td>Update employee by ID (all - for DIRECTOR, only operators from manager's city - for MANAGER).</td>
   <td>
     <pre>
       header: Content-Type: application/json
               Authorization: Bearer &lt;token&gt;
       body: {
                 "firstName": "BAR",
                 "lastName": "BAZ",
                 "email": "bar_baz@visacenter.com",
                 "phoneNumber": "375293003030",
                 "password": "password123",
                 "position": "MANAGER",
                 "city": "GRODNO"
             }
     </pre>
   </td>
   <td>
    If ok:
    <pre>
       header: Status Code: 201 Created
       body: {
                 "firstName": "BAR",
                 "lastName": "BAZ",
                 "email": "bar_baz@visacenter.com",
                 "phoneNumber": "375293003030",
                 "position": "MANAGER",
                 "city": "GRODNO",
                 "_links": {
                     "self": {
                         "href": "http://localhost:8080/employees/2"
                     },
                     "employees": {
                         "href": "http://localhost:8080/employees{?city,position,lastName,page,size,sort}",
                         "templated": true
                     }
                 } 
             }
    </pre>
    If form filling is incorrect - validation failed:
    <pre>
       header: Status Code: 406 Not Acceptable
       body: {
               "fieldErrors": [
                 {
                   "field": "phoneNumber",
                            "message": "Phone number should contain code and number itself, e.g. 375291001010."
                 },
                 {
                 "field": "firstName",
                            "message": "Only uppercase latin letters, spaces and dashes are allowed."
                 }
               ] 
             }
    </pre>
    If any field is absent:
    <pre>
        header: Status Code: 400 Bad Request
        body: The form filled incorrectly.
    </pre>
    If another employee has an email equals to the one indicated in form for updating:
    <pre>
      header: Status Code: 400 Bad Request
      body: There is another employee in the database with such email.
    </pre>
    If not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find employee with ID = &lt;ID&gt;. / Cannot find operator in &lt;manager's_city&gt; with ID = &lt;ID&gt;.
    </pre>
    If DIRECTOR tried to change his/her position:
    <pre>
      header: Status Code: 400 Bad Request
      body: The logged employee cannot change his/her position.
    </pre>
    If MANAGER tried to change operator's position or city:
    <pre>
       header: Status Code: 400 Bad Request
       body: A manager cannot change operator's position or city.
    </pre>
  </td>
</tr>
<tr>
  <td>DELETE</td>
  <td>/employees/{id}</td>
  <td>DIRECTOR, MANAGER</td>
  <td>Delete employee by ID (all - for DIRECTOR, only operators from manager's city - for MANAGER).</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 200 OK
    </pre>
    If not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find employee with ID = &lt;ID&gt;. / Cannot find operator in &lt;manager's_city&gt; with ID = &lt;ID&gt;.
    </pre>
    If DIRECTOR tried to delete him-/herself:
    <pre>
      header: Status Code: 400 Bad Request
      body: The logged employee cannot delete him-/herself.
    </pre>
</tr>
</table>

<h4>Managing required visa documents' info by DIRECTOR</h4>
<table>
<thead>
  <tr>
    <th>Method</th>
    <th>Endpoint</th>
    <th>Access</th>
    <th>Description</th>
    <th>Request</th>
    <th>Response</th>
  </tr>
</thead>
<tr>
  <td>POST</td>
  <td>/documents-info</td>
  <td>DIRECTOR</td>
  <td>Add new required visa document's info.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
      body: {
              "visaType": "C",
              "occupation": "EMPLOYED",
              "docDescription": "Valid passport with an expiry date of at least six months."
            }
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 201 Created
      body: {
                "visaType": "C",
                "occupation": "EMPLOYED",
                "docDescription": "Valid passport with an expiry date of at least six months.",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/documents-info/1"
                    },
                    "visaDocumentsInfo": {
                        "href": "http://localhost:8080/documents-info{?visaType,occupation,docDescription,page,size,sort}",
                        "templated": true
                    }
                }
            }
    </pre>
    If any field is absent:
    <pre>
      header: Status Code: 400 Bad Request
      body: The form filled incorrectly.
    </pre>
  </td>
</tr>
<tr>
  <td>GET</td>
  <td>/documents-info</td>
  <td>DIRECTOR</td>
  <td>Get all required visa documents' info.<br />
      Filtering is available by following parameters: visaType, occupation, docDescription. Also by word fragment, e.g. <i>?docDescription=pass</i> -> all entries with words containing "pass".<br />
      Sorting and paging are available.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 200 OK
      body: {
                "_embedded": {
                    "visaDocumentsInfo": [
                        {
                            "visaType": "C",
                            "occupation": "EMPLOYED",
                            "docDescription": "Valid passport with an expiry date of at least six months.",
                            "_links": {
                                "self": {
                                    "href": "http://localhost:8080/documents-info/1"
                                },
                                "visaDocumentsInfo": {
                                    "href": "http://localhost:8080/documents-info{?visaType,occupation,docDescription,page,size,sort}",
                                    "templated": true
                                }
                            }
                        },
                        {
                            "visaType": "D",
                            "occupation": "UNEMPLOYED",
                            "docDescription": "Two actual photos 3.5x4.5, face 80%, optical glasses are allowed.",
                            "_links": {
                                "self": {
                                    "href": "http://localhost:8080/documents-info/2"
                                },
                                "visaDocumentsInfo": {
                                    "href": "http://localhost:8080/documents-info{?visaType,occupation,docDescription,page,size,sort}",
                                    "templated": true
                                }
                            }
                        }
                    ]
                },
                "_links": {
                   "self": {
                        "href": "http://localhost:8080/documents-info?page=0&size=10&sort=visaType,asc"
                    }
                },
                "page": {
                    "size": 10,
                    "totalElements": 2,
                    "totalPages": 1,
                    "number": 0
                }
            }
    </pre>
    If not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find visa documents' information.
    </pre>
  </td>
</tr>
<tr>
  <td>GET</td>
  <td>/documents-info/{id}</td>
  <td>DIRECTOR</td>
  <td>Get required visa documents' info by ID.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 200 OK
      body: {
                "visaType": "C",
                "occupation": "EMPLOYED",
                "docDescription": "Valid passport with an expiry date of at least six months.",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/documents-info/1"
                    },
                    "visaDocumentsInfo": {
                        "href": "http://localhost:8080/documents-info{?visaType,occupation,docDescription,page,size,sort}",
                        "templated": true
                    }
                }
            }
    </pre>
    If not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find visa documents' information with ID = &lt;visa_doc's_ID&gt;.
    </pre>
  </td>
</tr>
<tr>
  <td>PUT</td>
  <td>/documents-info/{id}</td>
  <td>DIRECTOR</td>
  <td>Update required visa document's info by ID.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
      body: {
              "visaType": "D",
              "occupation": "UNEMPLOYED",
              "docDescription": "Three actual photos 3.5x4.5, face 80%, optical glasses are allowed."
            }
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 201 Created
      body: {
                "visaType": "D",
                "occupation": "UNEMPLOYED",
                "docDescription": "Three actual photos 3.5x4.5, face 80%, optical glasses are allowed.",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/documents-info/2"
                    },
                    "visaDocumentsInfo": {
                        "href": "http://localhost:8080/documents-info{?visaType,occupation,docDescription,page,size,sort}",
                        "templated": true
                    }
                }
            }
    </pre>
    If any field is absent:
    <pre>
      header: Status Code: 400 Bad Request
      body: The form filled incorrectly.
    </pre>
    If not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find visa document's information with ID = &lt;visa_doc's_ID&gt;.
    </pre>
  </td>
</tr>
<tr>
  <td>DELETE</td>
  <td>/documents-info/{id}</td>
  <td>DIRECTOR</td>
  <td>Delete required visa document's info by ID.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 200 OK
    </pre>
    If not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find visa document's information with ID = &lt;visa_doc's_ID&gt;.
    </pre>
</tr>
</table>

<h4>Managing client's data by OPERATOR</h4>
<table>
<thead>
  <tr>
    <th>Method</th>
    <th>Endpoint</th>
    <th>Access</th>
    <th>Description</th>
    <th>Request</th>
    <th>Response</th>
  </tr>
</thead>
<tr>
  <td>GET</td>
  <td>/clients</td>
  <td>OPERATOR</td>
  <td>Get all clients' data.<br />
    Filtering is available by following parameters (client's fields and fields of nested entities - visa application
    and visa): lastName, passportId, email, phoneNumber, requiredVisaType, appointmentCity, appointmentDate,
    appointmentTime, visaApplicationStatus, visaNumber, issuedVisaType, issueDate, expiryDate.<br />
    Sorting and paging are available.
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 200 OK
      body: {
                "_embedded": {
                    "clients": [
                        {
                            "firstName": "SARAH",
                            "lastName": "CONNOR",
                            "email": "sarah_connor@skynet.net",
                            "phoneNumber": "375442002020",
                            "dateOfBirth": "1970-06-06",
                            "passportId": "0202PSPID20002020",
                            "occupation": "UNEMPLOYED",
                            "_links": {
                                "self": {
                                    "href": "http://localhost:8080/clients/2"
                                },
                                "clients": {
                                    "href": "http://localhost:8080/clients{?lastName,passportId,email,phoneNumber,requiredVisaType,appointmentCity,appointmentDate,appointmentTime,visaApplicationStatus,visaNumber,issuedVisaType,issueDate,expiryDate,page,size,sort}",
                                    "templated": true
                                }
                            }
                        },
                        {
                            "firstName": "JOHN",
                            "lastName": "DOE",
                            "email": "john_doe@gmail.com",
                            "phoneNumber": "375291001010",
                            "dateOfBirth": "1980-10-10",
                            "passportId": "0101PSPID10001010",
                            "occupation": "EMPLOYED",
                            "_links": {
                                "self": {
                                    "href": "http://localhost:8080/clients/3"
                                },
                                "clients": {
                                    "href": "http://localhost:8080/clients{?lastName,passportId,email,phoneNumber,requiredVisaType,appointmentCity,appointmentDate,appointmentTime,visaApplicationStatus,visaNumber,issuedVisaType,issueDate,expiryDate,page,size,sort}",
                                    "templated": true
                                }
                            }
                        }
                    ]
                },
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/clients?page=0&size=10&sort=lastName,asc"
                    }
                },
                    "page": {
                    "size": 10,
                    "totalElements": 2,
                    "totalPages": 1,
                    "number": 0
                }
            }
    </pre>
    If not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find clients.
    </pre>
  </td>
</tr>
<tr>
  <td>GET</td>
  <td>/clients/{id}</td>
  <td>OPERATOR</td>
  <td>Get client's data by ID.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 200 OK
      body: {
                "firstName": "SARA",
                "lastName": "CONNOR",
                "email": "sarah_connor@skynet.net",
                "phoneNumber": "375292002020",
                "dateOfBirth": "1970-06-06",
                "passportId": "0202PSPID20002020",
                "occupation": "UNEMPLOYED",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/clients/2"
                    },
                    "clients": {
                        "href": "http://localhost:8080/clients{?lastName,passportId,email,phoneNumber,requiredVisaType,appointmentCity,appointmentDate,appointmentTime,visaApplicationStatus,visaNumber,issuedVisaType,issueDate,expiryDate,page,size,sort}",
                        "templated": true
                    }
                }
            }
    </pre>
    If not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find client with ID = &lt;client's_ID&gt;.
    </pre>
  </td>
</tr>
<tr>
  <td>PUT</td>
  <td>/clients/{id}</td>
  <td>OPERATOR</td>
  <td>Update client's data by ID.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
      body: {
                "firstName": "KYLE",
                "lastName": "REESE",
                "email": "kyle_reese@skynet.net",
                "phoneNumber": "375293003030",
                "dateOfBirth": "1968-10-06",
                "passportId": "0303PSPID30003030",
                "occupation": "EMPLOYED"
            }
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 201 Created
      body: {
                "firstName": "KYLE",
                "lastName": "REESE",
                "email": "kyle_reese@skynet.net",
                "phoneNumber": "375293003030",
                "dateOfBirth": "1968-10-06",
                "passportId": "0303PSPID30003030",
                "occupation": "EMPLOYED",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/clients/2"
                    },
                    "clients": {
                        "href": "http://localhost:8080/clients{?lastName,passportId,email,phoneNumber,requiredVisaType,appointmentCity,appointmentDate,appointmentTime,visaApplicationStatus,visaNumber,issuedVisaType,issueDate,expiryDate,page,size,sort}",
                        "templated": true
                    }
                }
            }
    </pre>
    If form filling is incorrect - validation failed:
    <pre>
      header: Status Code: 406 Not Acceptable
      body: {
              "fieldErrors": [
                {
                  "field": "phoneNumber",
                           "message": "Phone number should contain code and number itself, e.g. 375291001010."
                },
                {
                  "field": "firstName",
                           "message": "Only uppercase latin letters, spaces and dashes are allowed."
                }
              ] 
            }
    </pre>
    If any field is absent:
    <pre>
      header: Status Code: 400 Bad Request
      body: The form filled incorrectly.
    </pre>
    If not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find client with ID = &lt;client's_ID&gt;.
    </pre>
    If another client has a passport ID and/or email equals to those indicated in form for updating:
    <pre>
      header: Status: 400 Bad Request
      body: There is another client in the database with such email and/or passport ID.
    </pre>
  </td>
</tr>
<tr>
  <td>DELETE</td>
  <td>/clients/{id}</td>
  <td>OPERATOR</td>
  <td>Delete client's data by ID.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 200 OK
    </pre>
    If not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find client with ID = &lt;client's_ID&gt;.
    </pre>
</tr>
</table>
  
<h4>Managing visa applications by OPERATOR</h4>
<table>
<thead>
  <tr>
    <th>Method</th>
    <th>Endpoint</th>
    <th>Access</th>
    <th>Description</th>
    <th>Request</th>
    <th>Response</th>
  </tr>
</thead>
<tr>
  <td>GET</td>
  <td>/applications</td>
  <td>OPERATOR, MANAGER</td>
  <td>By default (without filtering parameters), get visa applications with status BOOKED in operator's / manager's city.<br />
    With filtering parameters, get all matching visa applications. Filtering is available by following parameters (all visa application's fields and client's fields): requiredVisaType, appointmentCity, appointmentDate, appointmentTime, visaApplicationStatus, lastName, passportId, email, phoneNumber.<br />
    Sorting and paging are available.
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 200 OK
      body: {
                "_embedded": {
                    "applications": [
                        {
                            "requiredVisaType": "C",
                            "city": "MINSK",
                            "appointmentDate": "2020-04-01",
                            "appointmentTime": "10:45",
                            "visaApplicationStatus": "BOOKED",
                            "_links": {
                                "self": {
                                    "href": "http://localhost:8080/clients/3/applications/1"
                                },
                                "applications": {
                                    "href": "http://localhost:8080/applications{?requiredVisaType,appointmentCity,appointmentDate,appointmentTime,visaApplicationStatus,lastName,passportId,email,phoneNumber,page,size,sort}",
                                    "templated": true
                                }
                            }
                        },
                        {
                            "requiredVisaType": "C",
                            "city": "MINSK",
                            "appointmentDate": "2020-05-01",
                            "appointmentTime": "11:00",
                            "visaApplicationStatus": "BOOKED",
                            "_links": {
                                "self": {
                                    "href": "http://localhost:8080/clients/2/applications/2"
                                },
                                "applications": {
                                    "href": "http://localhost:8080/applications{?requiredVisaType,appointmentCity,appointmentDate,appointmentTime,visaApplicationStatus,lastName,passportId,email,phoneNumber,page,size,sort}",
                                    "templated": true
                                }
                            }
                        }
                    ]
                },
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/applications?page=0&size=10&sort=requiredVisaType,asc"
                    }
                },
                "page": {
                    "size": 10,
                    "totalElements": 2,
                    "totalPages": 1,
                    "number": 0
                }
            }
    </pre>
    If not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find visa applications.
    </pre>
  </td>
</tr>
<tr>
  <td>GET</td>
  <td>clients/{clientId}/applications/{applicationId}</td>
  <td>OPERATOR, MANAGER</td>
  <td>Get specific client's visa application by ID.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 200 OK
      body: {
                "firstName": "KYLE",
                "lastName": "REESE",
                "email": "kyle_reese@skynet.net",
                "phoneNumber": "375293003030",
                "dateOfBirth": "1968-10-06",
                "passportId": "0303PSPID30003030",
                "occupation": "EMPLOYED",
                "visaApplication": {
                    "requiredVisaType": "C",
                    "city": "GRODNO",
                    "appointmentDate": "2020-05-01",
                    "appointmentTime": "11:00",
                    "visaApplicationStatus": "BOOKED"
                },
                "requiredDocs": [],
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/clients/2/applications/2"
                    },
                    "applications": {
                        "href": "http://localhost:8080/applications{?requiredVisaType,appointmentCity,appointmentDate,appointmentTime,visaApplicationStatus,lastName,passportId,email,phoneNumber,page,size,sort}",
                        "templated": true
                    }
                }
            }
    </pre>
    If not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find visa application with ID = &lt;visa_application's_ID&gt; for client with ID = &lt;client's_ID&gt;.
    </pre>
  </td>
</tr>
<tr>
  <td>PUT</td>
  <td>clients/{clientId}/applications/{applicationId}</td>
  <td>OPERATOR</td>
  <td>Update client's visa application by ID. OPERATOR can update only visa application's from operator's city.<br />
    If status == BOOKED and application is in future, operator can update all the fields, but status only on appointment date and not together with other fields.<br />
    If status == DOCS_RECEIVED, PENDING, CONFIRMED or DENIED, operator can update only status - other filled fields will be ignored and remain the same as in database.<br />
    If status == DID_NOT_COME or ISSUED, visa application is archived and cannot be updated.<br />
    If status == DOCS_INCOMPLETE and visa application's appointment date is not gone, only its status can be updated, otherwise it is archived and cannot be updated.<br />
    Status DID_NOT_COME is set automatically everyday at 23:59 to the visa applications, which status remained as BOOKED till that time of today (see class app.services.util.VisitChecker, is called from app.Runner).<br />
    New status is saved into the table applications_status_history.
  </td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
      body: {
                "requiredVisaType": "B",
                "city": "BREST",
                "appointmentDate": "2020-05-20",
                "appointmentTime": "13:00",
                "visaApplicationStatus": "BOOKED"
            }
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 201 Created
      body: {
                "firstName": "JOHN",
                "lastName": "DOE",
                "email": "john_doe@gmail.com",
                "phoneNumber": "375291001010",
                "dateOfBirth": "1980-10-10",
                "passportId": "0101PSPID10001010",
                "occupation": "EMPLOYED",
                "visaApplication": {
                    "requiredVisaType": "B",
                    "city": "MINSK",
                    "appointmentDate": "2020-05-20",
                    "appointmentTime": "13:00",
                    "visaApplicationStatus": "BOOKED",
                    "_links": {
                        "self": {
                            "href": "http://localhost:8080/clients/3/applications/1"
                        },
                        "applications": {
                            "href": "http://localhost:8080/applications{?requiredVisaType,appointmentCity,appointmentDate,appointmentTime,visaApplicationStatus,lastName,passportId,email,phoneNumber,page,size,sort}",
                            "templated": true
                        }
                    }
                }
            }
    </pre>
    If form filling is incorrect - validation failed:
    <pre>
      header: Status Code: 406 Not Acceptable
      body: {
                "fieldErrors": [
                    {
                        "field": "appointmentDate",
                        "message": "Appointment date should be selected in range of 180 days from today."
                    }
                ]
            }
    </pre>
    If any field is absent:
    <pre>
      header: Status Code: 400 Bad Request
      body: The form filled incorrectly.
    </pre>
    If not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find visa application with ID = &lt;visa_application's_ID&gt; for client with ID = &lt;client's_ID&gt;.
    </pre>
    If OPERATOR tried to update visa application not from operator's city:
    <pre>
      header: Status Code: 400 Bad Request
      body: You can update visa applications only in &lt;operator's_city&gt;.
    </pre>
    If there is another application with fields <i>appointmentCity</i>, <i>appointmentDate</i> and <i>appointmentTime</i> equals to those indicated in form for updating:
    <pre>
      header: Status Code: 400 Bad Request
      body: There is another appointment already booked in &lt;new_appointmentCity&gt; on &lt;new_appointmentDate&gt; at
            &lt;new_appointmentTime&gt;.
    </pre>
    If OPERATOR tried to update visa application's status from BOOKED (or if it is in past) to another one together with other fields:
    <pre>
      header: Status Code: 400 Bad Request
      body: You cannot change booked visa application's required visa type, appointment city, date or time if you are going
            to change its status or if it is in past but not yet automatically set to DID_NOT_COME status.
    </pre>
    If OPERATOR tried to update visa application's status from BOOKED to another one before its appointment date:
    <pre>
      header: Status Code: 400 Bad Request
      body: Status of the visa application with ID = &lt;visa_application's_ID&gt; cannot be updated because its appointment
            date has not come yet.
    </pre>
    If OPERATOR tried to change visa application's status from DOCS_RECEIVED, PENDING, CONFIRMED or DENIED to BOOKED or DOCS_INCOMPLETE:
    <pre>
      header: Status Code: 400 Bad Request
      body: Status &lt;new_visaApplicationStatus&gt; cannot be set to the visa application
            with ID = &lt;visa_application's_ID&gt; because its current status is &lt;status_from_DB&gt; already.
    </pre>
    If OPERATOR tried to change visa application's status from DOCS_INCOMPLETE (if it is in past) to another one:
    <pre>
      header: Status Code: 400 Bad Request
      body: Visa application with ID = &lt;visa_application's_ID&gt; cannot be updated due to the expiration of the
            appointment date considering its status DOCS_INCOMPLETE.
    </pre>
    If OPERATOR tried to update visa application with status DID_NOT_COME or ISSUED:
    <pre>
      header: Status Code: 400 Bad Request
      body: Visa application with ID = &lt;visa_application's_ID&gt; cannot be updated due to its status &lt;DID_NOT_COME or ISSUED&gt;.
    </pre>
  </td>
</tr>
<tr>
  <td>DELETE</td>
  <td>clients/{clientId}/applications/{applicationId}</td>
  <td>OPERATOR</td>
  <td>Delete client's visa application by ID - only from OPERATOR'S city and if its status is BOOKED or if its appointment date and time are in future.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 200 OK
    </pre>
    If not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find visa application with ID = &lt;visa_application's_ID&gt; for client with ID = &lt;client's_ID&gt;.
    </pre>
    If OPERATOR tried to ignore restrictions:
    <pre>
      header: Status Code: 400 Bad Request
      body: You can delete visa application only if its appointment city is &lt;operator's_city&gt; and if its status is 
            BOOKED or if its appointment date and time are in future.
    </pre>
</tr>
<tr>
  <td>GET</td>
  <td>clients/{clientId}/applications/{applicationId}/history</td>
  <td>OPERATOR, MANAGER</td>
  <td>Get specific client's visa application's status' updates history ordered by field <i>settingDate</i> in chronological order.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
    </pre>
  </td>
  <td>
    <pre>
      header: Status Code: 200 OK
      body: {
                "firstName": "JOHN",
                "lastName": "DOE",
                "email": "john_doe@gmail.com",
                "phoneNumber": "375291001010",
                "dateOfBirth": "1980-10-10",
                "passportId": "0101PSPID10001010",
                "occupation": "EMPLOYED",
                "application": {
                    "requiredVisaType": "B",
                    "city": "MINSK",
                    "appointmentDate": "2020-03-24",
                    "appointmentTime": "13:00",
                    "visaApplicationStatus": "ISSUED",
                    "history": [
                        {
                            "applicationStatus": "DOCS_RECEIVED",
                            "settingDate": "2020-03-24T21:58:40.619+0300",
                            "operatorLink": "http://localhost:8080/employees/2"
                        },
                        {
                            "applicationStatus": "PENDING",
                            "settingDate": "2020-03-24T21:59:07.127+0300",
                            "operatorLink": "http://localhost:8080/employees/2"
                        },
                        {
                            "applicationStatus": "CONFIRMED",
                            "settingDate": "2020-03-24T21:59:17.437+0300",
                            "operatorLink": "http://localhost:8080/employees/2"
                        },
                        {
                            "applicationStatus": "ISSUED",
                            "settingDate": "2020-03-24T21:59:22.527+0300",
                            "operatorLink": "http://localhost:8080/employees/2"
                        }
                    ]
                },
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/clients/3/applications/1/history"
                    },
                    "applications": {
                        "href": "http://localhost:8080/applications{?requiredVisaType,appointmentCity,appointmentDate,appointmentTime,visaApplicationStatus,lastName,passportId,email,phoneNumber,page,size,sort}",
                        "templated": true
                    }
                }
            }
    </pre>
  </td>
</tr>
</table>

<h4>Managing visa's data by OPERATOR</h4>
<table>
<thead>
  <tr>
    <th>Method</th>
    <th>Endpoint</th>
    <th>Access</th>
    <th>Description</th>
    <th>Request</th>
    <th>Response</th>
  </tr>
</thead>
<tr>
  <td>POST</td>
  <td>/clients/{clientId}/new-visa</td>
  <td>OPERATOR</td>
  <td>Add new visa to a client.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
      body: {
              "visaNumber": "100VSPL001",
              "visaType": "C",
              "issueDate": "2019-06-06",
              "expiryDate": "2020-06-05"
            }
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 201 Created
      body: {
                "firstName": "KYLE",
                "lastName": "REESE",
                "email": "kyle_reese@skynet.net",
                "phoneNumber": "375293003030",
                "dateOfBirth": "1968-10-06",
                "passportId": "0303PSPID30003030",
                "occupation": "EMPLOYED",
                "visa": {
                    "visaNumber": "100VSPL001",
                    "visaType": "C",
                    "issueDate": "2019-06-06",
                    "expiryDate": "2020-06-05",
                    "_links": {
                        "self": {
                            "href": "http://localhost:8080/clients/2/visas/1"
                        },
                        "visas": {
                            "href": "http://localhost:8080/visas{?visaNumber,issuedVisaType,issueDate,expiryDate,lastName,passportId,email,phoneNumber,page,size,sort}",
                            "templated": true
                        }
                    }
                }
            }
    </pre>
    If any field is absent:
    <pre>
      header: Status Code: 400 Bad Request
      body: The form filled incorrectly.
    </pre>
    If indicated issue date is later than expiry date:
    <pre>
      header: Status Code: 400 Bad Request
      body: The expiry date should be later than issue date.
    </pre>
    If client is not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find client with ID = &lt;client's_ID&gt;.
    </pre>
  </td>
</tr>
<tr>
  <td>GET</td>
  <td>/visas</td>
  <td>OPERATOR, MANAGER, DIRECTOR</td>
  <td>Get all visas.<br />
    Filtering is available by following parameters (all visa's and client's fields): visaNumber, issuedVisaType, issueDate, expiryDate, lastName, passportId, email, phoneNumber.<br />
    Sorting and paging are available.
  </td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 200 OK
      body: {
                "_embedded": {
                    "visas": [
                        {
                            "visaNumber": "100VSPL001",
                            "visaType": "C",
                            "issueDate": "2019-06-06",
                            "expiryDate": "2020-06-05",
                            "_links": {
                                "self": {
                                    "href": "http://localhost:8080/clients/2/visas/1"
                                },
                                "visas": {
                                    "href": "http://localhost:8080/visas{?visaNumber,issuedVisaType,issueDate,expiryDate,lastName,passportId,email,phoneNumber,page,size,sort}",
                                    "templated": true
                                }
                            }
                        },
                        {
                            "visaNumber": "392VSPL222",
                            "visaType": "C",
                            "issueDate": "2018-03-04",
                            "expiryDate": "2019-03-03",
                            "_links": {
                                "self": {
                                    "href": "http://localhost:8080/clients/2/visas/3"
                                },
                                "visas": {
                                    "href": "http://localhost:8080/visas{?visaNumber,issuedVisaType,issueDate,expiryDate,lastName,passportId,email,phoneNumber,page,size,sort}",
                                    "templated": true
                                }
                            }
                        }
                    ]
                },
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/visas?page=0&size=10&sort=expiryDate,desc"
                    }
                },
                "page": {
                    "size": 10,
                    "totalElements": 2,
                    "totalPages": 1,
                    "number": 0
                }
            }
    </pre>
    If not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find visas.
    </pre>
  </td>
</tr>
<tr>
  <td>GET</td>
  <td>/clients/{clientId}/visas/{visaId}</td>
  <td>OPERATOR, MANAGER, DIRECTOR</td>
  <td>Get specific client's visa by ID.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 200 OK
      body: {
                "firstName": "KYLE",
                "lastName": "REESE",
                "email": "kyle_reese@skynet.net",
                "phoneNumber": "375293003030",
                "dateOfBirth": "1968-10-06",
                "passportId": "0303PSPID30003030",
                "occupation": "EMPLOYED",
                "visa": {
                    "visaNumber": "100VSPL001",
                    "visaType": "C",
                    "issueDate": "2019-06-06",
                    "expiryDate": "2020-06-05",
                    "_links": {
                        "self": {
                            "href": "http://localhost:8080/clients/2/visas/1"
                        },
                        "visas": {
                            "href": "http://localhost:8080/visas{?visaNumber,issuedVisaType,issueDate,expiryDate,lastName,passportId,email,phoneNumber,page,size,sort}",
                            "templated": true
                        }
                    }
                }
            }
    </pre>
    If not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find visa with ID = &lt;visa's_ID&gt; for client with ID = &lt;client's_ID&gt;.
    </pre>
  </td>
</tr>
<tr>
  <td>PUT</td>
  <td>/clients/{clientId}/visas/{visaId}</td>
  <td>OPERATOR</td>
  <td>Update client's visa by ID.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
      body: {
              "visaNumber": "100VSPL001",
              "visaType": "C",
              "issueDate": "2019-06-06",
              "expiryDate": "2020-06-05"
            }
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 201 Created
      body: {
              "visaNumber": "100VSPL001",
              "visaType": "C",
              "issueDate": "2019-06-06",
              "expiryDate": "2020-06-05"
            }
    </pre>
    If any field is absent:
    <pre>
      header: Status Code: 400 Bad Request
      body: The form filled incorrectly.
    </pre>
    If not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find visa with ID = &lt;visa's_ID&gt; for client with ID = &lt;client's_ID&gt;.
    </pre>
    If indicated issue date is later than expiry date:
    <pre>
      header: Status Code: 400 Bad Request
      body: The expiry date should be later than issue date.
    </pre>
  </td>
</tr>
<tr>
  <td>DELETE</td>
  <td>/clients/{clientId}/visas/{visaId}</td>
  <td>OPERATOR</td>
  <td>Delete client's visa by ID.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 200 OK
    </pre>
    If not found:
    <pre>
      header: Status Code: 404 Not Found
      body: Cannot find visa with ID = &lt;visa's_ID&gt; for client with ID = &lt;client's_ID&gt;.
    </pre>
</tr>
</table>

<h4>Managing profile and visa application by CLIENT</h4>
<table>
<thead>
  <tr>
    <th>Method</th>
    <th>Endpoint</th>
    <th>Access</th>
    <th>Description</th>
    <th>Request</th>
    <th>Response</th>
  </tr>
</thead>
  <tr>
  <td>GET</td>
  <td>/my-profile</td>
  <td>CLIENT</td>
  <td>Get logged client's profile data.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
    </pre>
  </td>
  <td>
    <pre>
      header: Status Code: 200 OK
      body: {
                "firstName": "KYLE",
                "lastName": "REESE",
                "email": "kyle_reese@skynet.net",
                "phoneNumber": "375293003030",
                "dateOfBirth": "1968-10-06",
                "passportId": "0303PSPID30003030",
                "occupation": "EMPLOYED",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/my-profile"
                    }
                }
            }
    </pre>
  </td>
</tr>
<tr>
  <td>PUT</td>
  <td>/my-profile</td>
  <td>CLIENT</td>
  <td>Update logged client's profile data (all fields except password and personal data processing agreement).</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
      body: {
                "firstName": "KYLE",
                "lastName": "REESE",
                "email": "kyle_reese@skynet.net",
                "phoneNumber": "375293003030",
                "dateOfBirth": "1968-10-06",
                "passportId": "0303PSPID30003030",
                "occupation": "ENTREPRENEUR"
            }
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 201 Created
      body: {
                "firstName": "KYLE",
                "lastName": "REESE",
                "email": "kyle_reese@skynet.net",
                "phoneNumber": "375293003030",
                "dateOfBirth": "1968-10-06",
                "passportId": "0303PSPID30003030",
                "occupation": "ENTREPRENEUR"
            }
    </pre>
    If form filling is incorrect - validation failed:
    <pre>
      header: Status Code: 406 Not Acceptable
      body: {
                "fieldErrors": [
                    {
                        "field": "phoneNumber",
                        "message": "Phone number should contain code and number itself, e.g. 375291001010."
                    },
                    {
                        "field": "firstName",
                        "message": "Only uppercase latin letters, spaces and dashes are allowed."
                    }
                ]
            }
    </pre>
    If any field is absent:
    <pre>
      header: Status Code: 400 Bad Request
      body: The form filled incorrectly.
    </pre>
    If another client has a passport ID and/or email equals to those indicated in form for updating:
    <pre>
      header: Status Code: 400 Bad Request
      body: There is another client in the database with such email and/or passport ID.
    </pre>
  </td>
</tr>
<tr>
  <td>POST</td>
  <td>/my-visa-application</td>
  <td>CLIENT</td>
  <td>Book new appointment = create new visa application.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
      body: {
              "requiredVisaType": "C",
              "city": "MINSK",
              "appointmentDate": "2020-05-01",
              "appointmentTime": "12:00"
            }
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 201 Created
      body: {
                "requiredVisaType": "C",
                "city": "MINSK",
                "appointmentDate": "2020-05-01",
                "appointmentTime": "12:00",
                "visaApplicationStatus": "BOOKED",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/my-visa-application"
                    }
                }
            }
    </pre>
    If appointment date and/or appointment time filled incorrectly:
    <pre>
      header: Status Code: 406 Not Acceptable
      body: {
                "fieldErrors": [
                    {
                        "field": "appointmentTime",
                        "message": "Appointment time should be between 09:00 and 17:00 with step of 15 minutes."
                    },
                    {
                        "field": "appointmentDate",
                        "message": "Appointment date should be selected in range of 180 days from today."
                    }
                ]
            }
    </pre>
    If any field is absent:
    <pre>
      header: Status Code: 400 Bad Request
      body: The form filled incorrectly.
    </pre>
    If client already has visa application with statuses BOOKED, DOCS_RECEIVED, PENDING, CONFIRMED or DENIED:
    <pre>
      header: Status Code: 400 Bad Request
      body: You already have visa application in processing.
    </pre>
    If there is another appointment with fields <i>city</i>, <i>appointmentDate</i> and <i>appointmentTime</i> equals to those indicated in form for inserting:
    <pre>
      header: Status Code: 400 Bad Request
      body: There is another appointment already booked in &lt;city&gt; on &lt;appointmentDate&gt; at &lt;appointmentTime&gt;.
    </pre>
  </td>
</tr>
<tr>
  <td>GET</td>
  <td>/my-visa-application</td>
  <td>CLIENT</td>
  <td>Get logged client's last visa application.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
    </pre>
  </td>
  <td>
    If logged client's last visa application has statuses DOCS_RECEIVED, PENDING, CONFIRMED or DENIED, only last visa application with its status will be displayed:
    <pre>
      header: Status Code: 200 OK
      body: {
                "requiredVisaType": "C",
                "city": "GRODNO",
                "appointmentDate": "2020-02-01",
                "appointmentTime": "11:00",
                "visaApplicationStatus": "PENDING",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/my-visa-application"
                    }
                }
            }
    </pre>
  If logged client's last visa application has status BOOKED, last visa application + documents list + disabled (booked
  by other clients) dates and time for updating last booked visa application will be displayed:
  <pre>
    header: Status Code: 200 OK
    body: [
              {
                  "requiredVisaType": "C",
                  "city": "GRODNO",
                  "appointmentDate": "2020-05-01",
                  "appointmentTime": "11:00",
                  "visaApplicationStatus": "BOOKED",
                  "requiredDocs": [
                      "Valid passport with an expiry date of at least six months.",
                      "Two actual photos 3.5x4.5, face 80%, optical glasses are allowed."
                  ],
                  "links": [
                      {
                          "rel": "self",
                          "href": "http://localhost:8080/my-visa-application"
                      }
                  ]
              },
              [
                  {
                      "city": "MINSK",
                      "disabledTimeByDate": {
                          "2020-05-24": [
                              "13:00"
                          ]
                      },
                      "disabledDates": []
                  },
                  {
                      "city": "GOMEL",
                      "disabledTimeByDate": {},
                      "disabledDates": []
                  },
                  {
                      "city": "MOGILEV",
                      "disabledTimeByDate": {},
                      "disabledDates": []
                  },
                  {
                      "city": "BREST",
                      "disabledTimeByDate": {},
                      "disabledDates": []
                  },
                  {
                      "city": "GRODNO",
                      "disabledTimeByDate": {
                          "2020-05-01": []
                      },
                      "disabledDates": [
                          "2020-05-01"
                      ]
                  }
              ]
          ]
  </pre>
  If logged client has no visa applications at all or his last visa application has statuses DID_NOT_COME, DOCS_INCOMPLETE
  or ISSUED disabled dates and time for adding new visa application will be displayed:
  <pre>
    header: Status Code: 200 OK
    body: [
              {
                  "city": "MINSK",
                  "disabledTimeByDate": {
                      "2020-05-24": [
                          "13:00"
                      ]
                  },
                  "disabledDates": []
              },
              {
                  "city": "GOMEL",
                  "disabledTimeByDate": {},
                  "disabledDates": []
              },
              {
                  "city": "MOGILEV",
                  "disabledTimeByDate": {},
                  "disabledDates": []
              },
              {
                  "city": "BREST",
                  "disabledTimeByDate": {},
                  "disabledDates": []
              },
              {
                  "city": "GRODNO",
                  "disabledTimeByDate": {},
                  "disabledDates": []
              }
          ]
  </pre>
  </td>
</tr>
<tr>
  <td>PUT</td>
  <td>/my-visa-application</td>
  <td>CLIENT</td>
  <td>Update logged client's last visa application only if it has status BOOKED.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
      body: {
              "requiredVisaType": "C",
              "city": "GRODNO",
              "appointmentDate": "2020-04-10",
              "appointmentTime": "09:45"
            }
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 201 Created
      body: {
                "requiredVisaType": "C",
                "city": "GRODNO",
                "appointmentDate": "2020-04-10",
                "appointmentTime": "09:45",
                "visaApplicationStatus": "BOOKED",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/my-visa-application"
                    }
                }
            }
    </pre>
    If appointment date and/or appointment time filled incorrectly:
    <pre>
      header: Status Code: 406 Not Acceptable
      body: {
                "fieldErrors": [
                    {
                        "field": "appointmentTime",
                        "message": "Appointment time should be between 09:00 and 17:00 with step of 15 minutes."
                    },
                    {
                        "field": "appointmentDate",
                        "message": "Appointment date should be selected in range of 180 days from today."
                    }
                ]
            }
    </pre>
    If any field is absent:
    <pre>
      header: Status Code: 400 Bad Request
      body: The form filled incorrectly.
    </pre>
    If there is another appointment with fields <i>city</i>, <i>appointmentDate</i> and <i>appointmentTime</i> equals to those indicated in form for inserting:
    <pre>
      header: Status Code: 400 Bad Request
      body: There is another appointment already booked in &lt;city&gt; on &lt;appointmentDate&gt; at &lt;appointmentTime&gt;.
    </pre>
    If logged client's last visa application doesn't have status BOOKED:
    <pre>
      header: Status Code: 400 Bad Request
      body: You have no booked visa applications.
    </pre>
  </td>
</tr>
<tr>
  <td>DELETE</td>
  <td>/my-visa-application</td>
  <td>CLIENT</td>
  <td>Delete logged client's last visa application only if it has status BOOKED.</td>
  <td>
    <pre>
      header: Content-Type: application/json
              Authorization: Bearer &lt;token&gt;
    </pre>
  </td>
  <td>
    If ok:
    <pre>
      header: Status Code: 200 OK
    </pre>
    If logged client's last visa application doesn't have status BOOKED:
    <pre>
      header: Status Code: 400 Bad Request
      body: You have no booked visa applications.
    </pre>
</tr>
</table>
