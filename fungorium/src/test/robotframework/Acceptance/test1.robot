*** Settings ***
Library           javalib-core
Library           BuiltIn

*** Variables ***
${INITIAL_TTL}    3

*** Test Cases ***
Spore Decay Decreases Time To Live
    # Create a tecton instance using Text_Base (assumed to implement Tecton_Class)
    ${tecton}=    Evaluate    new com.coderunnerlovagjai.app.Tecton_Base()   
    # Instantiate Basic_Spore with the Text_Base tecton instance.
    ${spore}=     Evaluate    new com.coderunnerlovagjai.app.Basic_Spore(${tecton})   
    ${ttl_before}=    Call Method    ${spore}    get_timeToLive
    Should Be Equal As Integers    ${ttl_before}    ${INITIAL_TTL}
    # Decay the spore (reduces TTL by 1)
    Call Method    ${spore}    decay
    ${ttl_after}=     Call Method    ${spore}    get_timeToLive
    Should Be Equal As Integers    ${ttl_after}     2