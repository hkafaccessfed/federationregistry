/*
 *  A Grails/Hibernate compatible environment for SAML2 metadata types with application specific 
 *  data extensions as appropriate.
 * 
 *  Copyright (C) 2010 Australian Access Federation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package fedreg.core

/*
 * @author Bradley Beddoes
 */
class EncryptionMethod  {

	String algorithm
	String keySize
	String oaeParams
	
	Date dateCreated
	Date lastUpdated

	static belongsTo = [keyDescriptor: KeyDescriptor]

	static constraints = {
		algorithm(nullable:false, blank:false)
		oaeParams(nullable: true)
		dateCreated(nullable:true)
		lastUpdated(nullable:true)
	}

	public String toString() {	"encryptionmethod:[id:$id, algorithm: $algorithm]" }
}
