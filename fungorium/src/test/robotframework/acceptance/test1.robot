*** Settings ***
Library    BuiltIn
Library    S

*** Variables ***
${TECTON_CLASS}         com.coderunnerlovagjai.app.Tecton_Basic
${BASIC_SPORE}          com.coderunnerlovagjai.app.Basic_Spore
${SPORE_SPEED}          com.coderunnerlovagjai.app.Spore_Speed
${SPORE_SLOWING}        com.coderunnerlovagjai.app.Spore_Slowing
${SPORE_PARALYSING}     com.coderunnerlovagjai.app.Spore_Paralysing
${SPORE_DUPLICATOR}     com.coderunnerlovagjai.app.Spore_Duplicator

*** Test Cases ***
Test Basic Spore Parameterized Constructor
    [Documentation]    Create a Basic_Spore with a target tecton and verify its timeToLive value.
    ${tecton}=    Evaluate    new ${TECTON_CLASS}()    modules=java
    ${spore}=     Evaluate    new ${BASIC_SPORE}(${tecton})    modules=java
    ${ttl}=       Evaluate    ${spore}.get_timeToLive()    modules=java
    Should Be Equal    ${ttl}    3
    ${sporeFromTecton}=    Evaluate    ${tecton}.get_Spore()    modules=java
    Should Be Equal    ${spore}    ${sporeFromTecton}

Test Spore Speed Constructor
    [Documentation]    Create a Spore_Speed with a target tecton and verify its timeToLive.
    ${tecton}=    Evaluate    new ${TECTON_CLASS}()    modules=java
    ${sporeSpeed}=    Evaluate    new ${SPORE_SPEED}(${tecton})    modules=java
    ${ttl}=       Evaluate    ${sporeSpeed}.get_timeToLive()    modules=java
    Should Be Equal    ${ttl}    3

Test Spore Slowing Constructor
    [Documentation]    Create a Spore_Slowing with a target tecton and verify its timeToLive.
    ${tecton}=    Evaluate    new ${TECTON_CLASS}()    modules=java
    ${sporeSlowing}=    Evaluate    new ${SPORE_SLOWING}(${tecton})    modules=java
    ${ttl}=       Evaluate    ${sporeSlowing}.get_timeToLive()    modules=java
    Should Be Equal    ${ttl}    3

Test Spore Paralysing Constructor
    [Documentation]    Create a Spore_Paralysing with a target tecton and verify its timeToLive.
    ${tecton}=    Evaluate    new ${TECTON_CLASS}()    modules=java
    ${sporeParalysing}=    Evaluate    new ${SPORE_PARALYSING}(${tecton})    modules=java
    ${ttl}=       Evaluate    ${sporeParalysing}.get_timeToLive()    modules=java
    Should Be Equal    ${ttl}    3

Test Spore Duplicator Constructor
    [Documentation]    Create a Spore_Duplicator with a target tecton and verify its timeToLive.
    ${tecton}=    Evaluate    new ${TECTON_CLASS}()    modules=java
    ${sporeDuplicator}=    Evaluate    new ${SPORE_DUPLICATOR}(${tecton})    modules=java
    ${ttl}=       Evaluate    ${sporeDuplicator}.get_timeToLive()    modules=java
    Should Be Equal    ${ttl}    3
